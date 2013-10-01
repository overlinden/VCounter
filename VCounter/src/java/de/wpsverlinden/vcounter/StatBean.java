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
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean
public class StatBean {

    @EJB
    private StatDAO sdao;

    @EJB
    private HitDAO hdao;

    @ManagedProperty(value = "#{param.id}")
    private long counterId;

    public long getCounterId() {
        return counterId;
    }

    public void setCounterId(long counterId) {
        this.counterId = counterId;
    }

    public Stat getTodaysData() {
        GregorianCalendar day = new GregorianCalendar();
        return sdao.getAggregatedDataFor(counterId, Constants.DAY_FORMAT.format(day.getTime()));
    }

    public Stat getYesterdaysData() {
        GregorianCalendar day = new GregorianCalendar();
        day.add(Calendar.DAY_OF_YEAR, -1);
        return sdao.getAggregatedDataFor(counterId, Constants.DAY_FORMAT.format(day.getTime()));
    }

    public Stat getCurrentMonthData() {
        GregorianCalendar month = new GregorianCalendar();
        return sdao.getAggregatedDataFor(counterId, Constants.MONTH_FORMAT.format(month.getTime()));
    }

    public Stat getLastMonthData() {
        GregorianCalendar month = new GregorianCalendar();
        month.add(Calendar.MONTH, -1);
        return sdao.getAggregatedDataFor(counterId, Constants.MONTH_FORMAT.format(month.getTime()));
    }

    public Stat getCurrentYearData() {
        GregorianCalendar year = new GregorianCalendar();
        return sdao.getAggregatedDataFor(counterId, Constants.YEAR_FORMAT.format(year.getTime()));
    }

    public Stat getLastYearData() {
        GregorianCalendar year = new GregorianCalendar();
        year.add(Calendar.YEAR, -1);
        return sdao.getAggregatedDataFor(counterId, Constants.YEAR_FORMAT.format(year.getTime()));
    }

    public Stat getTotalData() {
        return sdao.getAggregatedDataFor(counterId, "");
    }

    public int getOnlineUsers() {
        GregorianCalendar start = new GregorianCalendar();
        start.add(Calendar.MINUTE, -5);
        return hdao.getOnlineUsers(counterId, Constants.TIME_FORMAT.format(start.getTime()));
    }

    public List<Hit> getLastUsers() {
        List<Hit> hits = hdao.getLastUsers(counterId);
        for (Hit h : hits) {
            try {
                h.setTimestamp(Constants.TIME_FORMAT_DEU.format(Constants.TIME_FORMAT.parse(h.getTimestamp())));
            } catch (ParseException ex) {
            }
            if (h.getUserAgent().getName().length() > 60) {
                h.getUserAgent().setName(h.getUserAgent().getName().substring(0, 59).concat("..."));
            }
        }
        return hits;
    }
}
