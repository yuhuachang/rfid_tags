package com.taiwankk.rfid.tags;

import static org.junit.Assert.*;

import org.junit.Test;

import com.taiwankk.rfid.tags.Sgtin96.EpcHeader;

public class Sgtin96Test {

    @Test
    public void testHeader() {
        Sgtin96 tag = new Sgtin96();
        assertEquals(EpcHeader.SGTIN_96, tag.getHeader());
        assertEquals("30", tag.toHexString().substring(0, 2));
        assertEquals("urn:epc:tag:sgtin-96:", tag.toString().substring(0, 21));
    }

    @Test
    public void testDefaultFilter() {
        Sgtin96 tag = new Sgtin96();
        assertEquals(1, tag.getFilter());
    }
    
    @Test
    public void testFilter() {
        Sgtin96 tag = new Sgtin96();
        for (int i = 0; i < 8; i++) {
            tag.setFilter(i);
            assertEquals(i, tag.getFilter());
        }
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testFilterRange1() {
        Sgtin96 tag = new Sgtin96();
        tag.setFilter(-1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testFilterRange2() {
        Sgtin96 tag = new Sgtin96();
        tag.setFilter(8);
    }
    
    @Test
    public void testDefaultPartition() {
        Sgtin96 tag = new Sgtin96();
        assertEquals(3, tag.getPartition());
    }
    
    @Test
    public void testPartition() {
        Sgtin96 tag = new Sgtin96();
        for (int i = 0; i < 7; i++) {
            tag.setPartition(i);
            assertEquals(i, tag.getPartition());
        }
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testPartitionRange1() {
        Sgtin96 tag = new Sgtin96();
        tag.setPartition(-1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testPartitionRange2() {
        Sgtin96 tag = new Sgtin96();
        tag.setPartition(7);
    }
    
    @Test
    public void testCompanyPrefixItemReference() {
        Sgtin96 tag = new Sgtin96();
        tag.setPartition(0);

        for (int i = 0, x = 12, y = 1; i <= 6; i++, x--, y++) {

            tag.setPartition(i);
            
            long companyPrefix = Long.parseLong(repeats('9', x));
            tag.setCompanyPrefix(companyPrefix);
            assertEquals(companyPrefix, tag.getCompanyPrefix());
            
            long itemReference = Long.parseLong(repeats('9', y));
            tag.setItemReference(itemReference);
            assertEquals(itemReference, tag.getItemReference());
            
            try {
                tag.setCompanyPrefix(-1);
                fail();
            } catch (IllegalArgumentException e) {
                // good
            }
            
            try {
                tag.setItemReference(-1);
                fail();
            } catch (IllegalArgumentException e) {
                // good
            }
            
            try {
                tag.setCompanyPrefix(Long.parseLong(repeats('9', x + 1)));
                fail();
            } catch (IllegalArgumentException e) {
                // good
            }
            
            try {
                tag.setItemReference(Long.parseLong(repeats('9', y + 1)));
                fail();
            } catch (IllegalArgumentException e) {
                // good
            }
        }
    }

    @Test
    public void testSerial() {
        Sgtin96 tag = new Sgtin96();
        long serial = Long.parseLong(repeats('1', 38), 2);
        tag.setSerial(serial);
        assertEquals(serial, tag.getSerial());
        assertEquals("3FFFFFFFFF", tag.toHexString().substring(14));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSerialRange1() {
        Sgtin96 tag = new Sgtin96();
        long serial = Long.parseLong("1" + repeats('0', 38), 2);
        tag.setSerial(serial);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSerialRange2() {
        Sgtin96 tag = new Sgtin96();
        tag.setSerial(-1);
    }
    
    private String repeats(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
