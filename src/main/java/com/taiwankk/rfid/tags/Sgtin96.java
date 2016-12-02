package com.taiwankk.rfid.tags;

import java.util.BitSet;

import org.springframework.util.Assert;

/**
 * http://www.gs1.org/sites/default/files/docs/epc/TDS_1_9_Standard.pdf<br>
 * http://www.gs1.org/BC-EPC-guidelines
 * 
 * 14.5.1.1.SGTIN-96 Coding Table
 * <table border="1">
 * <tr>
 * <td>URI Template</td>
 * <td colspan="6">urn:epc:tag:sgtin-96:Fiter.CompanyPrefix.ItemReference.Serial
 * </td>
 * </tr>
 * <tr>
 * <td>Total Bits</td>
 * <td colspan="6">96</td>
 * </tr>
 * <tr>
 * <td>Logical Segment</td>
 * <td>EPC Header</td>
 * <td>Filter</td>
 * <td>Partition</td>
 * <td>Company Prefix</td>
 * <td>Item Reference</td>
 * <td>Serial</td>
 * </tr>
 * <tr>
 * <td>Logical Segment Bit Count</td>
 * <td>8</td>
 * <td>3</td>
 * <td>3</td>
 * <td>20-40</td>
 * <td>24-4</td>
 * <td>38</td>
 * </tr>
 * </table>
 * Total Bits: 96
 * 
 * EPC Header: 8 bits Filter: 3 bits Partition: 3 bits GS1 Company Prefix: 20~40
 * bits base on partition settings Item Reference: 24~4 bits base on partition
 * settings Serial: 38 bits
 * <p>
 * SGTIN-96 Partitions.
 * <table border="1">
 * <tr>
 * <td>Partition Value (P)</td>
 * <td colspan="2">Company Prefix</td>
 * <td colspan="2">Item Reference and Indicator Digit</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Bits (M)</td>
 * <td>Digits (L)</td>
 * <td>Bits (N)</td>
 * <td>Digits</td>
 * </tr>
 * <tr><td>0</td><td>40</td><td>12</td><td>4</td><td>1</td></tr>
 * <tr><td>1</td><td>37</td><td>11</td><td>7</td><td>2</td></tr>
 * <tr><td>2</td><td>34</td><td>10</td><td>10</td><td>3</td></tr>
 * <tr><td>3</td><td>30</td><td>9</td><td>14</td><td>4</td></tr>
 * <tr><td>4</td><td>27</td><td>8</td><td>17</td><td>5</td></tr>
 * <tr><td>5</td><td>24</td><td>7</td><td>20</td><td>6</td></tr>
 * <tr><td>6</td><td>20</td><td>6</td><td>24</td><td>7</td></tr>
 * </table>
 * 
 * @author Yu-Hua Chang
 */
public class Sgtin96 {

    public enum EpcHeader {
        // binary value | hex value | total bits | name
        // 0011 0000    | 30        | 96         | SGTIN-96
        SGTIN_96
    }

    private final int TOTAL_BITS = 96;

    private static final int[] PARTITION_REF = { 40, 37, 34, 30, 27, 24, 20 };

    private static final int[] PARTITION_DIGIT_REF = { 12, 11, 10, 9, 8, 7, 6 };

    private final BitSet bits;

    public Sgtin96() {
        bits = new BitSet();

        // Header SGTIN-96
        bits.set(2, true);
        bits.set(3, true);

        // Default Point of Sale (POS) Trade Item.
        setFilter(1);

        // Default company prefix 9 digits.
        setPartition(3);
    }

    protected Sgtin96(BitSet bits) {
        this.bits = bits;
        
        // Check if is SGTIN_96
        Assert.isTrue(!this.bits.get(0));
        Assert.isTrue(!this.bits.get(1));
        Assert.isTrue(this.bits.get(2));
        Assert.isTrue(this.bits.get(3));
        Assert.isTrue(!this.bits.get(4));
        Assert.isTrue(!this.bits.get(5));
        Assert.isTrue(!this.bits.get(6));
        Assert.isTrue(!this.bits.get(7));
    }

    public EpcHeader getHeader() {
        return EpcHeader.SGTIN_96;
    }

    /**
     * <table border="1">
     * <tr><td>Type                                 </td><td>Binary Value</td></tr>
     * <tr><td>All Others                           </td><td>000</td></tr>
     * <tr><td>Retail Consumer Trade Item           </td><td>001</td></tr>
     * <tr><td>Standard Trade Item Grouping         </td><td>010</td></tr>
     * <tr><td>Single Shipping/ Consumer Trade Item </td><td>011</td></tr>
     * <tr><td>Reserved                             </td><td>100</td></tr>
     * <tr><td>Reserved                             </td><td>101</td></tr>
     * <tr><td>Reserved                             </td><td>110</td></tr>
     * <tr><td>Reserved                             </td><td>111</td></tr>
     * </table>
     * @param filter
     */
    public void setFilter(int filter) {
        if (filter < 0 || filter > 7) {
            throw new IndexOutOfBoundsException();
        }
        setLong(8, 11, filter);
    }

    /**
     * <table border="1">
     * <tr><td>Type                                 </td><td>Binary Value</td></tr>
     * <tr><td>All Others                           </td><td>000</td></tr>
     * <tr><td>Retail Consumer Trade Item           </td><td>001</td></tr>
     * <tr><td>Standard Trade Item Grouping         </td><td>010</td></tr>
     * <tr><td>Single Shipping/ Consumer Trade Item </td><td>011</td></tr>
     * <tr><td>Reserved                             </td><td>100</td></tr>
     * <tr><td>Reserved                             </td><td>101</td></tr>
     * <tr><td>Reserved                             </td><td>110</td></tr>
     * <tr><td>Reserved                             </td><td>111</td></tr>
     * </table>
     * @return filter
     */
    public int getFilter() {
        return (int) getLong(8, 11);
    }

    /**
     * Check Sgtin-96 Partition Value
     * @param partition
     */
    public void setPartition(int partition) {
        if (partition < 0 || partition > 6) {
            throw new IndexOutOfBoundsException();
        }
        setLong(11, 14, partition);
    }

    /**
     * Check Sgtin-96 Partition Value
     * @return partition
     */
    public int getPartition() {
        return (int) getLong(11, 14);
    }

    public void setCompanyPrefix(long companyPrefix) {
        if (companyPrefix < 0 || companyPrefix >= (long) Math.pow(2, PARTITION_REF[getPartition()])) {
            throw new IllegalArgumentException("company prefix " + companyPrefix + " out of range.");
        }
        if (("" + companyPrefix).length() > PARTITION_DIGIT_REF[getPartition()]) {
            throw new IllegalArgumentException("company prefix " + companyPrefix + " has too many digits. max is " + PARTITION_DIGIT_REF[getPartition()]);
        }
        setLong(14, 14 + PARTITION_REF[getPartition()], companyPrefix);
    }

    public long getCompanyPrefix() {
        return getLong(14, 14 + PARTITION_REF[getPartition()]);
    }

    public void setItemReference(long itemReference) {
        if (itemReference < 0 || itemReference >= (long) Math.pow(2, 44 - PARTITION_REF[getPartition()])) {
            throw new IllegalArgumentException("item reference " + itemReference + " out of range.");
        }
        if (("" + itemReference).length() > (13 - PARTITION_DIGIT_REF[getPartition()])) {
            throw new IllegalArgumentException("item reference " + itemReference + " has too many digits. max is " + (13 - PARTITION_DIGIT_REF[getPartition()]));
        }
        setLong(14 + PARTITION_REF[getPartition()], 58, itemReference);
    }

    public long getItemReference() {
        return getLong(14 + PARTITION_REF[getPartition()], 58);
    }

    public void setSerial(long serial) {
        if (serial < 0 || serial >= (long) Math.pow(2, 38)) {
            throw new IllegalArgumentException("serial " + serial + " out of range.");
        }
        setLong(58, 96, serial);
    }

    public long getSerial() {
        return getLong(58, 96);
    }

    protected final void setLong(int start, int end, long value) {
        if (end <= start) {
            throw new IndexOutOfBoundsException("set bits from " + start + " to " + end + " out of range.");
        }
        for (int bitIndex = end - 1, shiftBits = 0; bitIndex >= start && shiftBits < 64; bitIndex--, shiftBits++) {
            bits.set(bitIndex, (value >>> shiftBits & 1) == 1);
        }
    }

    protected final long getLong(int start, int end) {
        if (end <= start) {
            throw new IndexOutOfBoundsException("get bits from " + start + " to " + end + " out of range.");
        }
        long value = 0L;
        for (int bitIndex = start, shiftBits = end - start - 1; bitIndex < end
                && shiftBits >= 0; bitIndex++, shiftBits--) {
            value += (bits.get(bitIndex) ? 1L : 0L) << shiftBits;
        }
        return value;
    }

    public String toBitString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TOTAL_BITS; i++) {
            if (i % 4 == 0) {
                sb.append(" ");
            }
            sb.append(bits.get(i) ? "1" : "0");
        }
        return sb.toString().trim();
    }

    // to HEX
    public String toHexString(int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i += 4) {
            int v = (int) getLong(i, i + 4);
            if (v < 10) {
                sb.append(v);
            } else {
                switch (v) {
                case 10:
                    sb.append('A');
                    break;
                case 11:
                    sb.append('B');
                    break;
                case 12:
                    sb.append('C');
                    break;
                case 13:
                    sb.append('D');
                    break;
                case 14:
                    sb.append('E');
                    break;
                case 15:
                    sb.append('F');
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * @return Tag in hex format.
     */
    public String toHexString() {
        return toHexString(0, TOTAL_BITS);
    }

    /**
     * {@inheritDoc}
     * Return tag urn.
     */
    @Override
    public String toString() {
        return String.format("urn:epc:tag:sgtin-96:%d.%d.%d.%d",
                getFilter(),
                getCompanyPrefix(),
                getItemReference(),
                getSerial());
    }

    public static final Sgtin96 valueOf(String hex) {
        BitSet bits = BitSet.valueOf(new long[] { Long.valueOf(hex.substring(2), 16) });
        return new Sgtin96(bits);
    }
}
