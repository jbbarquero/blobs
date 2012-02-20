// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.malsolo.blobs.domain;

import com.malsolo.blobs.domain.NoDocumento;
import com.malsolo.blobs.domain.NoDocumentoDataOnDemand;
import com.malsolo.blobs.domain.NoDocumentoIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect NoDocumentoIntegrationTest_Roo_IntegrationTest {
    
    declare @type: NoDocumentoIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: NoDocumentoIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: NoDocumentoIntegrationTest: @Transactional;
    
    @Autowired
    private NoDocumentoDataOnDemand NoDocumentoIntegrationTest.dod;
    
    @Test
    public void NoDocumentoIntegrationTest.testCountNoDocumentoes() {
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", dod.getRandomNoDocumento());
        long count = NoDocumento.countNoDocumentoes();
        Assert.assertTrue("Counter for 'NoDocumento' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testFindNoDocumento() {
        NoDocumento obj = dod.getRandomNoDocumento();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to provide an identifier", id);
        obj = NoDocumento.findNoDocumento(id);
        Assert.assertNotNull("Find method for 'NoDocumento' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'NoDocumento' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testFindAllNoDocumentoes() {
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", dod.getRandomNoDocumento());
        long count = NoDocumento.countNoDocumentoes();
        Assert.assertTrue("Too expensive to perform a find all test for 'NoDocumento', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<NoDocumento> result = NoDocumento.findAllNoDocumentoes();
        Assert.assertNotNull("Find all method for 'NoDocumento' illegally returned null", result);
        Assert.assertTrue("Find all method for 'NoDocumento' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testFindNoDocumentoEntries() {
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", dod.getRandomNoDocumento());
        long count = NoDocumento.countNoDocumentoes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<NoDocumento> result = NoDocumento.findNoDocumentoEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'NoDocumento' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'NoDocumento' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testFlush() {
        NoDocumento obj = dod.getRandomNoDocumento();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to provide an identifier", id);
        obj = NoDocumento.findNoDocumento(id);
        Assert.assertNotNull("Find method for 'NoDocumento' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyNoDocumento(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'NoDocumento' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testMergeUpdate() {
        NoDocumento obj = dod.getRandomNoDocumento();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to provide an identifier", id);
        obj = NoDocumento.findNoDocumento(id);
        boolean modified =  dod.modifyNoDocumento(obj);
        Integer currentVersion = obj.getVersion();
        NoDocumento merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'NoDocumento' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", dod.getRandomNoDocumento());
        NoDocumento obj = dod.getNewTransientNoDocumento(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'NoDocumento' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'NoDocumento' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void NoDocumentoIntegrationTest.testRemove() {
        NoDocumento obj = dod.getRandomNoDocumento();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'NoDocumento' failed to provide an identifier", id);
        obj = NoDocumento.findNoDocumento(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'NoDocumento' with identifier '" + id + "'", NoDocumento.findNoDocumento(id));
    }
    
}
