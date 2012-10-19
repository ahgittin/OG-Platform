/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.config.impl;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import javax.time.Instant;

import net.sf.ehcache.CacheManager;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ObjectIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.config.ConfigDocument;
import com.opengamma.master.config.ConfigSearchRequest;
import com.opengamma.master.config.ConfigSearchResult;
import com.opengamma.util.ehcache.EHCacheUtils;

/**
 * Test {@link EHCachingMasterConfigSource}.
 */
@Test
public class EHCachingMasterConfigSourceTest {
  
  private static final VersionCorrection VC = VersionCorrection.LATEST;
  private static final ExternalId CONFIG = ExternalId.of("Test", "sec1");
  private static final String CONFIG_NAME = "Test";
  
  private UnitTestConfigMaster _underlyingConfigMaster;
  private EHCachingMasterConfigSource _cachingSource;
  
  private static final ConfigItem<ExternalId> ITEM;
  static {
    ConfigItem<ExternalId> item = new ConfigItem<ExternalId>(CONFIG);
    item.setName(CONFIG_NAME);
    item.setType(ExternalId.class);
    ITEM = item;
  }
 
  @BeforeMethod
  public void setUp() throws Exception {
    _underlyingConfigMaster = new UnitTestConfigMaster();
    CacheManager cm = EHCacheUtils.createCacheManager();
    EHCacheUtils.clear(cm, EHCachingMasterConfigSource.CONFIG_CACHE);
    _cachingSource = new EHCachingMasterConfigSource(_underlyingConfigMaster, cm);
  }

  public void getConfig_uniqueId() {
    UniqueId uniqueId = _underlyingConfigMaster.add(new ConfigDocument(ITEM)).getUniqueId();
    assertSame(_cachingSource.getConfig(ExternalId.class, uniqueId), CONFIG);
    assertSame(_cachingSource.getConfig(ExternalId.class, uniqueId), CONFIG);
    assertEquals(1, _underlyingConfigMaster.getCounter().get());
  }
  
  public void getConfig_objectId() {
    UniqueId uniqueId = _underlyingConfigMaster.add(new ConfigDocument(ITEM)).getUniqueId();
    assertSame(_cachingSource.getConfig(ExternalId.class, uniqueId.getObjectId(), VC), CONFIG);
    assertSame(_cachingSource.getConfig(ExternalId.class, uniqueId.getObjectId(), VC), CONFIG);
    assertEquals(1, _underlyingConfigMaster.getCounter().get());
  }
  
  public void getByName() {
    final VersionCorrection versionCorrection = VersionCorrection.of(Instant.now(), null);
    _underlyingConfigMaster.add(new ConfigDocument(ITEM));
    assertSame(_cachingSource.get(ExternalId.class, CONFIG_NAME, versionCorrection).getValue(), CONFIG);
    assertSame(_cachingSource.get(ExternalId.class, CONFIG_NAME, versionCorrection).getValue(), CONFIG);
    assertEquals(1, _underlyingConfigMaster.getCounter().get());
  }
  
  public void getLatestByName() {
    _underlyingConfigMaster.add(new ConfigDocument(ITEM));
    assertSame(_cachingSource.getLatestByName(ExternalId.class, CONFIG_NAME), CONFIG);
    assertSame(_cachingSource.getLatestByName(ExternalId.class, CONFIG_NAME), CONFIG);
    assertEquals(1, _underlyingConfigMaster.getCounter().get());
  }

  public void getConfigs() {
    final Collection<ExternalId> configs = Lists.newArrayList(CONFIG, CONFIG);
    _underlyingConfigMaster.add(new ConfigDocument(ITEM));
    _underlyingConfigMaster.add(new ConfigDocument(ITEM));
    
    assertTrue(configs.contains(_cachingSource.getConfig(ExternalId.class, CONFIG_NAME, VC)));
    assertTrue(configs.contains(_cachingSource.getConfig(ExternalId.class, CONFIG_NAME, VC)));
    assertEquals(1, _underlyingConfigMaster.getCounter().get());
  }
  
  public void getLatestByNameAfterUpdate() {
    ConfigDocument addedDoc = _underlyingConfigMaster.add(new ConfigDocument(ITEM));
    assertSame(_cachingSource.getLatestByName(ExternalId.class, CONFIG_NAME), CONFIG);
    assertSame(_cachingSource.getLatestByName(ExternalId.class, CONFIG_NAME), CONFIG);
    assertEquals(1, _underlyingConfigMaster.getCounter().get());
    
    final ExternalId lastestConfig = ExternalId.of ("Test", "sec1");
    addedDoc.setObject(ConfigItem.of(lastestConfig));
    _underlyingConfigMaster.update(addedDoc);
    assertSame(_cachingSource.getLatestByName(ExternalId.class, CONFIG_NAME), lastestConfig);
    assertSame(_cachingSource.getLatestByName(ExternalId.class, CONFIG_NAME), lastestConfig);
    assertEquals(2, _underlyingConfigMaster.getCounter().get());
  }
  
  private static class UnitTestConfigMaster extends InMemoryConfigMaster {
    
    private AtomicLong _counter = new AtomicLong(0);

    public AtomicLong getCounter() {
      return _counter;
    }

    @Override
    public ConfigDocument get(ObjectIdentifiable objectId, VersionCorrection versionCorrection) {
      _counter.getAndIncrement();
      return super.get(objectId, versionCorrection);
    }

    @Override
    public ConfigDocument get(UniqueId uniqueId) {
      _counter.getAndIncrement();
      return super.get(uniqueId);
    }

    @Override
    public ConfigSearchResult search(ConfigSearchRequest request) {
      _counter.getAndIncrement();
      return super.search(request);
    }       
  }
  
}
