package com.tenxdev.jsinterop.generator.generator;

import com.tenxdev.jsinterop.generator.errors.AbstractErrorHandler;
import com.tenxdev.jsinterop.generator.errors.DevNullErrorrHandler;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Model;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeMapperTest {

    private TypeMapper typeMapper;

    @Before
    public void init(){
        typeMapper= new TypeMapper(new Model(), new DevNullErrorrHandler());
    }

    @Test
    public void mapSequenceType() throws Exception {
        assertEquals("int[][]", typeMapper.mapType("sequence<sequence<int>>"));

    }

    @Test
    public void mapRecordType() throws Exception {
        assertEquals("Dictionary<long, double>", typeMapper.mapType("record<unisgnedlong, unrestricteddouble>"));

    }
}
