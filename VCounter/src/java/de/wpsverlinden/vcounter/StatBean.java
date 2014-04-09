/**
 *
 * VCounter - Der Besucherzähler ohne JavaScript!
 * Copyright (C) 2013 Oliver Verlinden (http://wps-verlinden.de)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.wpsverlinden.vcounter;

import de.wpsverlinden.vcounter.dao.HitDAO;
import de.wpsverlinden.vcounter.dao.StatDAO;
import de.wpsverlinden.vcounter.entities.Hit;
import de.wpsverlinden.vcounter.entities.Stat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@ManagedBean
public class StatBean {

    @Inject
    private StatDAO sdao;

    @Inject
    private HitDAO hdao;

    @ManagedProperty(value = "#{param.id}")
    private long counterId;
    
    private Stat currentMonthData, currentYearData, lastMonthData, lastYearData, todaysData, yesterdaysData, totalData;
    private List<Hit> lastUsers;
    private Integer onlineUsers;

    public long getCounterId() {
        return counterId;
    }

    public void setCounterId(long counterId) {
        this.counterId = counterId;
    }

    public Stat getTodaysData() {
        if (todaysData == null) {
            GregorianCalendar day = new GregorianCalendar();
            todaysData = sdao.getAggregatedDataFor(counterId, Constants.DAY_FORMAT.format(day.getTime()));
        }
        return todaysData;
    }

    public Stat getYesterdaysData() {
        if (yesterdaysData == null) {
            GregorianCalendar day = new GregorianCalendar();
            day.add(Calendar.DAY_OF_YEAR, -1);
            yesterdaysData = sdao.getAggregatedDataFor(counterId, Constants.DAY_FORMAT.format(day.getTime()));
        }
        return yesterdaysData;
    }

    public Stat getCurrentMonthData() {
        if (currentMonthData == null) {
            GregorianCalendar month = new GregorianCalendar();
            currentMonthData = sdao.getAggregatedDataFor(counterId, Constants.MONTH_FORMAT.format(month.getTime()));
        }
        return currentMonthData;
    }

    public Stat getLastMonthData() {
        if (lastMonthData == null) {
            GregorianCalendar month = new GregorianCalendar();
            month.add(Calendar.MONTH, -1);
            lastMonthData = sdao.getAggregatedDataFor(counterId, Constants.MONTH_FORMAT.format(month.getTime()));
        }
        return lastMonthData;
    }

    public Stat getCurrentYearData() {
        if (currentYearData == null) {
            GregorianCalendar year = new GregorianCalendar();
            currentYearData = sdao.getAggregatedDataFor(counterId, Constants.YEAR_FORMAT.format(year.getTime()));
        }
        return currentYearData;
    }

    public Stat getLastYearData() {
        if (lastYearData == null) {
            GregorianCalendar year = new GregorianCalendar();
            year.add(Calendar.YEAR, -1);
            lastYearData = sdao.getAggregatedDataFor(counterId, Constants.YEAR_FORMAT.format(year.getTime()));
        }
        return lastYearData;
    }

    public Stat getTotalData() {
        if (totalData == null) {
            totalData = sdao.getAggregatedDataFor(counterId, "");
        }
        return totalData;
    }

    public int getOnlineUsers() {
        if (onlineUsers == null) {
            GregorianCalendar start = new GregorianCalendar();
            start.add(Calendar.MINUTE, -5);
            onlineUsers = new Integer(hdao.getOnlineUsers(counterId, Constants.TIME_FORMAT.format(start.getTime())));
        }
        return onlineUsers.intValue();
    }

    public List<Hit> getLastUsers() {
        if (lastUsers == null) {
            lastUsers = hdao.getLastUsers(counterId);
            for (Hit h : lastUsers) {
                try {
                    h.setTimestamp(Constants.TIME_FORMAT_DEU.format(Constants.TIME_FORMAT.parse(h.getTimestamp())));
                } catch (ParseException ex) {
                }
                if (h.getUserAgent().getName().length() > 60) {
                    h.getUserAgent().setName(h.getUserAgent().getName().substring(0, 59).concat("..."));
                }
            }
        }
        return lastUsers;
    }
}
