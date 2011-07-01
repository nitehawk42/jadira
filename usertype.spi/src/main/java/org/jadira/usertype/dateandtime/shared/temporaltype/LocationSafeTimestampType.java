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
package org.jadira.usertype.dateandtime.shared.temporaltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.hibernate.HibernateException;

public class LocationSafeTimestampType extends AbstractLocationSafeUserType {

    private static final long serialVersionUID = -6374256465793823865L;

    @Override
    public Timestamp deepCopyNotNull(Object value) {
        return fromStringValue(toString(value));
    }

    @Override
    public Timestamp get(ResultSet rs, String name) throws SQLException {
        final Timestamp timestamp = rs.getTimestamp(name, getUtcCalendar());
        return timestamp;
    }

    @Override
    public void set(PreparedStatement st, Object timestampValue, int index) throws SQLException {
        if (!(timestampValue instanceof Timestamp)) {
            timestampValue = deepCopy(timestampValue);
        }
        st.setTimestamp(index, (Timestamp) timestampValue, getUtcCalendar());
    }

    @Override
    public Class<Timestamp> returnedClass() {
        return Timestamp.class;
    }

    @Override
    public int sqlType() {
        return Types.TIMESTAMP;
    }

    @Override
    public Timestamp fromStringValue(String stringValue) throws HibernateException {

        Timestamp ts = null;
        if (stringValue.length() == 10) {
            try {
                long time = java.sql.Date.valueOf(stringValue).getTime();
                ts = new Timestamp(time);
            } catch (IllegalArgumentException ex) {
                // Was not a java.sql.Date, let Timestamp handle this value
                ts = null;
            }
        }
        if (ts == null) {
            ts = Timestamp.valueOf(stringValue);
        }

        return ts;
    }

    @Override
    public String toString(Object timestampValue) throws HibernateException {
        if (timestampValue instanceof Timestamp) {
            ((Timestamp) timestampValue).toString();
        }
        return new Timestamp(((java.util.Date) timestampValue).getTime()).toString();
    }

    public String getName() {
        return "locationSafeTimestamp";
    }
}
