/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.position;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.id.Identifier;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.master.position.PositionSearchRequest;
import com.opengamma.master.position.PositionSearchResult;
import com.opengamma.util.db.PagingRequest;

/**
 * Tests QueryPositionDbPositionMasterWorker.
 */
public class QueryPositionDbPositionMasterWorkerSearchTest extends AbstractDbPositionMasterWorkerTest {
  // superclass sets up dummy database

  private static final Logger s_logger = LoggerFactory.getLogger(QueryPositionDbPositionMasterWorkerSearchTest.class);

  private DbPositionMasterWorker _worker;

  public QueryPositionDbPositionMasterWorkerSearchTest(String databaseType, String databaseVersion) {
    super(databaseType, databaseVersion);
    s_logger.info("running testcases for {}", databaseType);
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    _worker = new QueryPositionDbPositionMasterWorker();
    _worker.init(_posMaster);
  }

  @After
  public void tearDown() throws Exception {
    super.tearDown();
    _worker = null;
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_documents() {
    PositionSearchRequest request = new PositionSearchRequest();
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(1, test.getPaging().getFirstItem());
    assertEquals(Integer.MAX_VALUE, test.getPaging().getPagingSize());
    assertEquals(_totalPositions, test.getPaging().getTotalItems());
    
    assertEquals(_totalPositions, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_pageOne() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setPagingRequest(new PagingRequest(1, 2));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(1, test.getPaging().getFirstItem());
    assertEquals(2, test.getPaging().getPagingSize());
    assertEquals(_totalPositions, test.getPaging().getTotalItems());
    
    assertEquals(2, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
    assert120(test.getDocuments().get(1));
  }

  @Test
  public void test_search_pageTwo() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setPagingRequest(new PagingRequest(2, 2));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(3, test.getPaging().getFirstItem());
    assertEquals(2, test.getPaging().getPagingSize());
    assertEquals(_totalPositions, test.getPaging().getTotalItems());
    
    assertEquals(2, test.getDocuments().size());
    assert121(test.getDocuments().get(0));
    assert122(test.getDocuments().get(1));
  }

  @Test
  public void test_search_pageThree() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setPagingRequest(new PagingRequest(3, 2));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(5, test.getPaging().getFirstItem());
    assertEquals(2, test.getPaging().getPagingSize());
    assertEquals(_totalPositions, test.getPaging().getTotalItems());
    
    assertEquals(2, test.getDocuments().size());
    assert123(test.getDocuments().get(0));
    assert222(test.getDocuments().get(1));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_positionIds() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "120"));
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "221"));
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "9999"));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(2, test.getDocuments().size());
    assert120(test.getDocuments().get(0));
    assert222(test.getDocuments().get(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_search_positionIds_badSchemeValidOid() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.getPositionIds().add(UniqueIdentifier.of("Rubbish", "120"));
    _worker.search(request);
  }

  @Test
  public void test_search_tradeIds() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "402"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "403"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "407"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "9999"));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(2, test.getDocuments().size());
    assert122(test.getDocuments().get(0));
    assert222(test.getDocuments().get(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_search_tradeIds_badSchemeValidOid() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.getTradeIds().add(UniqueIdentifier.of("Rubbish", "402"));
    _worker.search(request);
  }

  @Test
  public void test_search_positionAndTradeIds_matchSome() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "120"));
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "122"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "402"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "403"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "407"));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(1, test.getDocuments().size());
    assert122(test.getDocuments().get(0));
  }

  @Test
  public void test_search_positionAndTradeIds_matchNone() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "120"));
    request.getPositionIds().add(UniqueIdentifier.of("DbPos", "121"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "402"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "403"));
    request.getTradeIds().add(UniqueIdentifier.of("DbPos", "407"));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(0, test.getDocuments().size());
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_providerNoMatch() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setProviderId(Identifier.of("A", "999"));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(0, test.getDocuments().size());
  }

  @Test
  public void test_search_providerFound() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setProviderId(Identifier.of("A", "121"));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(1, test.getDocuments().size());
    assert121(test.getDocuments().get(0));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_minQuantity_below() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setMinQuantity(BigDecimal.valueOf(50));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(6, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
    assert120(test.getDocuments().get(1));
    assert121(test.getDocuments().get(2));
    assert122(test.getDocuments().get(3));
    assert123(test.getDocuments().get(4));
    assert222(test.getDocuments().get(5));
  }

  @Test
  public void test_search_minQuantity_mid() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setMinQuantity(BigDecimal.valueOf(150));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(1, test.getDocuments().size());
    assert222(test.getDocuments().get(0));
  }

  @Test
  public void test_search_minQuantity_above() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setMinQuantity(BigDecimal.valueOf(450));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(0, test.getDocuments().size());
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_maxQuantity_below() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setMaxQuantity(BigDecimal.valueOf(50));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(0, test.getDocuments().size());
  }

  @Test
  public void test_search_maxQuantity_mid() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setMaxQuantity(BigDecimal.valueOf(150));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(5, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
    assert120(test.getDocuments().get(1));
    assert121(test.getDocuments().get(2));
    assert122(test.getDocuments().get(3));
    assert123(test.getDocuments().get(4));
  }

  @Test
  public void test_search_maxQuantity_above() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setMaxQuantity(BigDecimal.valueOf(450));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(6, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
    assert120(test.getDocuments().get(1));
    assert121(test.getDocuments().get(2));
    assert122(test.getDocuments().get(3));
    assert123(test.getDocuments().get(4));
    assert222(test.getDocuments().get(5));
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_search_versionAsOf_below() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setVersionAsOfInstant(_version1Instant.minusSeconds(5));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(0, test.getDocuments().size());
  }

  @Test
  public void test_search_versionAsOf_mid() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setVersionAsOfInstant(_version1Instant.plusSeconds(5));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(6, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
    assert120(test.getDocuments().get(1));
    assert121(test.getDocuments().get(2));
    assert122(test.getDocuments().get(3));
    assert123(test.getDocuments().get(4));
    assert221(test.getDocuments().get(5));  // old version
  }

  @Test
  public void test_search_versionAsOf_above() {
    PositionSearchRequest request = new PositionSearchRequest();
    request.setVersionAsOfInstant(_version2Instant.plusSeconds(5));
    PositionSearchResult test = _worker.search(request);
    
    assertEquals(6, test.getDocuments().size());
    assert100(test.getDocuments().get(0));
    assert120(test.getDocuments().get(1));
    assert121(test.getDocuments().get(2));
    assert122(test.getDocuments().get(3));
    assert123(test.getDocuments().get(4));
    assert222(test.getDocuments().get(5));  // new version
  }

  //-------------------------------------------------------------------------
  @Test
  public void test_toString() {
    assertEquals(_worker.getClass().getSimpleName() + "[DbPos]", _worker.toString());
  }

}
