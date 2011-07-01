/*
 *  Copyright 2010, 2011 Christopher Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.usertype.dateandtime.joda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.jadira.usertype.dateandtime.joda.testmodel.JodaDateTimeAsStringHolder;
import org.jadira.usertype.dateandtime.shared.dbunit.DatabaseCapable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPersistentDateTimeAsString extends DatabaseCapable {

    private static final DateTime[] dateTimes = new DateTime[] { 
        new DateTime(2004, 2, 25, 12, 11, 10, 0, DateTimeZone.forOffsetHours(4)).withZone(DateTimeZone.UTC), 
        new DateTime(1980, 3, 11, 13, 12, 11, 500, DateTimeZone.UTC), 
        null };

    private static EntityManagerFactory factory;

    @BeforeClass
    public static void setup() {
        factory = Persistence.createEntityManagerFactory("test1");
    }

    @AfterClass
    public static void tearDown() {
        factory.close();
    }

    @Test
    public void testPersist() {

        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();

        for (int i = 0; i < dateTimes.length; i++) {

            JodaDateTimeAsStringHolder item = new JodaDateTimeAsStringHolder();
            item.setId(i);
            item.setName("test_" + i);
            item.setDateTime(dateTimes[i]);

            manager.persist(item);
        }

        manager.flush();
        
        manager.getTransaction().commit();
        
        manager.close();

        manager = factory.createEntityManager();
        
        for (int i = 0; i < dateTimes.length; i++) {

            JodaDateTimeAsStringHolder item = manager.find(JodaDateTimeAsStringHolder.class, Long.valueOf(i));

            assertNotNull(item);
            assertEquals(i, item.getId());
            assertEquals("test_" + i, item.getName());
            if (dateTimes[i] == null) {
            	assertNull(item.getDateTime());
            } else {
            	assertEquals(dateTimes[i].toString(), item.getDateTime().toString());
            }
        }
        
        verifyDatabaseTable(manager, JodaDateTimeAsStringHolder.class.getAnnotation(Table.class).name());
        
        manager.close();
    }
}
