package com.personal.scheduler.data;

/**
 * Created by Jose Cortes on 12/04/15.
 * Basic Immutable class that represents key of the data structure for storing messages
 * It considers same key if the groups is the same
 * Comparing objects is done exclusively using the arriving time of the message
 */
public class BasicKey implements Comparable<BasicKey> {

    final long arrivingOrder;
    private final String group;

    /**
     * Constructs the key
     *
     * @param arrivingOrder
     * @param group
     */
    public BasicKey(final long arrivingOrder, final String group) {
        this.arrivingOrder = arrivingOrder;
        this.group = group;
    }

    /**
     * Equals method. Logical equallity is done using Only  the group message
     *
     * @param obj
     * @return boolean indicates logical equality
     */
    @Override
    public boolean equals(Object obj) {
        final BasicKey objkey = (BasicKey) obj;

        return this.getGroup().equals(objkey.getGroup());
    }

    /**
     * returns hascode for object. Equal objects will have equal hashcode
     *
     * @return the hashcode for the object
     */
    @Override
    public int hashCode() {
        return this.getGroup().hashCode();
    }

    /**
     * Internal comparator. will be ONLY based on arriving time
     *
     * @param o
     * @return int that indicates gt lt or equal
     */
    @Override
    public int compareTo(BasicKey o) {
        if (this.equals(o)) {
            return 0;
        }
        return (arrivingOrder > o.arrivingOrder) ? 1 : -1;
    }

    /**
     * Gets the message group for this key
     *
     * @return the message group
     */
    public String getGroup() {
        return group;
    }
}
