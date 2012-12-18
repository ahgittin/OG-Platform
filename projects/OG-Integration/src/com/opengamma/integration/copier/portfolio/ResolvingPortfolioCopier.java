/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.integration.copier.portfolio;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.opengamma.bbg.BloombergIdentifierProvider;
import com.opengamma.bbg.ReferenceDataProvider;
import com.opengamma.bbg.loader.BloombergHistoricalTimeSeriesLoader;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.id.ExternalId;
import com.opengamma.id.UniqueId;
import com.opengamma.integration.copier.portfolio.reader.PortfolioReader;
import com.opengamma.integration.copier.portfolio.writer.PortfolioWriter;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesMaster;
import com.opengamma.master.position.ManageablePosition;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.ObjectsPair;

/**
 * A portfolio copier that copies positions/securities from readers to the specified writer while resolving time-series.
 */
public class ResolvingPortfolioCopier implements PortfolioCopier {

  private static final Logger s_logger = LoggerFactory.getLogger(ResolvingPortfolioCopier.class);

  private HistoricalTimeSeriesMaster _htsMaster;
  private HistoricalTimeSeriesSource _bbgHtsSource;
  private ReferenceDataProvider _bbgRefDataProvider;
  private String _dataProvider;
  private String[] _dataFields;
  
  public ResolvingPortfolioCopier(
      HistoricalTimeSeriesMaster htsMaster,
      HistoricalTimeSeriesSource bbgHtsSource,
      ReferenceDataProvider bbgRefDataProvider,
      String dataProvider,
      String[] dataFields) {
    
    ArgumentChecker.notNull(htsMaster, "htsMaster");
    ArgumentChecker.notNull(bbgHtsSource, "bbgHtsSource");
    ArgumentChecker.notNull(bbgRefDataProvider, "bbgRefDataProvider");
    ArgumentChecker.notNull(dataProvider, "dataProvider");
    ArgumentChecker.notNull(dataFields, "dataFields");

    _htsMaster = htsMaster;
    _bbgHtsSource = bbgHtsSource;
    _bbgRefDataProvider = bbgRefDataProvider;
    _dataProvider = dataProvider;
    _dataFields = dataFields;    
  }
  
  @Override
  public void copy(PortfolioReader portfolioReader, PortfolioWriter portfolioWriter) {
    copy(portfolioReader, portfolioWriter, null);
  }

  @Override
  public void copy(PortfolioReader portfolioReader, PortfolioWriter portfolioWriter, PortfolioCopierVisitor visitor) {

    ArgumentChecker.notNull(portfolioWriter, "portfolioWriter");
    ArgumentChecker.notNull(portfolioReader, "portfolioReader");
    
    // Get bbg hts loader
    BloombergIdentifierProvider bbgIdentifierProvider = new BloombergIdentifierProvider(_bbgRefDataProvider);
    BloombergHistoricalTimeSeriesLoader bbgLoader = new BloombergHistoricalTimeSeriesLoader(_htsMaster, _bbgHtsSource, bbgIdentifierProvider);
    
    ObjectsPair<ManageablePosition, ManageableSecurity[]> next;

    // Read in next row, checking for EOF
    while ((next = portfolioReader.readNext()) != null) {
      
      // Is position and security data is available for the current row?
      if (next.getFirst() != null && next.getSecond() != null) {
        
        // Set current path
        String[] path = portfolioReader.getCurrentPath();
        portfolioWriter.setPath(path);
        
        // Load all relevant HTSes
        for (ManageableSecurity security : next.getSecond()) {
          resolveTimeSeries(bbgLoader, security, _dataFields, _dataProvider, visitor);
        }
        
        // Write position and security data
        ObjectsPair<ManageablePosition, ManageableSecurity[]> written = 
            portfolioWriter.writePosition(next.getFirst(), next.getSecond());
        
        if (visitor != null) {
          visitor.info(StringUtils.arrayToDelimitedString(path, "/"), written.getFirst(), written.getSecond());
        }
      } else {
        if (visitor != null) {
          if (next.getFirst() == null) {
            visitor.error("Could not load position");
          }
          if (next.getSecond() == null) {
            visitor.error("Could not load security(ies)");
          }
        }
      }
      
    }

    // Flush changes to portfolio master
    portfolioWriter.flush();
  }

  void resolveTimeSeries(BloombergHistoricalTimeSeriesLoader bbgLoader, ManageableSecurity security, String[] dataFields, String dataProvider, PortfolioCopierVisitor visitor) {
    for (String dataField : dataFields) {
      Set<ExternalId> ids = new HashSet<ExternalId>();
      ids = security.getExternalIdBundle().getExternalIds();
      Map<ExternalId, UniqueId> tsMap = null;
      for (ExternalId id : ids) {
        tsMap = bbgLoader.addTimeSeries(Collections.singleton(id), dataProvider, dataField, null, null);
        String message = "historical time series " + id.toString() + ", fields " + dataField + 
            " from " + dataProvider;
        if (tsMap.size() > 0) {
          s_logger.info("Loaded " + message + ": " + tsMap);
          if (visitor != null) {
            visitor.info("Loaded " + message);
          }
          break;
        }
      }
      if (tsMap == null || tsMap.size() == 0) {
        s_logger.warn("Could not load historical time series for security " + security);
        if (visitor != null) {
          visitor.error("Could not load historical time series for security " + security);
        }
      }
    }    
  }
  
  public HistoricalTimeSeriesMaster getHtsMaster() {
    return _htsMaster;
  }

  public void setHtsMaster(HistoricalTimeSeriesMaster htsMaster) {
    _htsMaster = htsMaster;
  }

  public HistoricalTimeSeriesSource getBbgHtsSource() {
    return _bbgHtsSource;
  }

  public void setBbgHtsSource(HistoricalTimeSeriesSource bbgHtsSource) {
    _bbgHtsSource = bbgHtsSource;
  }

  public ReferenceDataProvider getBbgRefDataProvider() {
    return _bbgRefDataProvider;
  }

  public void setBbgRefDataProvider(ReferenceDataProvider bbgRefDataProvider) {
    _bbgRefDataProvider = bbgRefDataProvider;
  }

  public String getDataProvider() {
    return _dataProvider;
  }

  public void setDataProvider(String dataProvider) {
    _dataProvider = dataProvider;
  }

  public String[] getDataFields() {
    return _dataFields;
  }

  public void setDataFields(String[] dataFields) {
    _dataFields = dataFields;
  }

}