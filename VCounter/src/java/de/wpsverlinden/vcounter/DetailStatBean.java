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

import de.wpsverlinden.vcounter.dao.*;
import de.wpsverlinden.vcounter.entities.Stat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean
public class DetailStatBean {

    @EJB
    private HitDAO hdao;

    @EJB
    private StatDAO sdao;

    @ManagedProperty(value = "#{param.id}")
    private long counterId;
    
    @ManagedProperty(value = "#{param.t}")
    private String interval;

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public long getCounterId() {
        return counterId;
    }

    public void setCounterId(long counterId) {
        this.counterId = counterId;
    }

    public List<Stat> getLast30() {
        List<Stat> stats = sdao.getLast30DataFor(counterId);
        for (Stat s : stats) {
            try {
                s.setDay(Constants.DAY_FORMAT_DEU.format(Constants.DAY_FORMAT.parse(s.getDay())));
            } catch (ParseException ex) {
            }
        }
        return stats;
    }

    public List<Stat> getTopTenHits() {
        List<Stat> stats = sdao.getTopTenHits(counterId, timeIntervalToConstrain());
        for (Stat s : stats) {
            try {
                s.setDay(Constants.DAY_FORMAT_DEU.format(Constants.DAY_FORMAT.parse(s.getDay())));
            } catch (ParseException ex) {
            }
        }
        return stats;
    }

    public List<Stat> getTopTenVisits() {
        List<Stat> stats = sdao.getTopTenVisits(counterId, timeIntervalToConstrain());
        for (Stat s : stats) {
            try {
                s.setDay(Constants.DAY_FORMAT_DEU.format(Constants.DAY_FORMAT.parse(s.getDay())));
            } catch (ParseException ex) {
            }
        }
        return stats;
    }

    public List<String[]> getTopTenUserAgents() {
        return hdao.getTopTenUserAgents(counterId, timeIntervalToConstrain());
    }

    public List<String[]> getTopTenReferers() {
        List<String[]> refs = hdao.getTopTenReferers(counterId, timeIntervalToConstrain());
        List<String[]> ret = new LinkedList<String[]>();
        for (Object[] in : refs) {
            String[] out = new String[4];
            if (((String) in[0]).length() > 60) {
                out[0] = ((String) in[0]).substring(0, 59).concat("...");
            } else {
                out[0] = (String) in[0];
            }
            out[1] = (String) in[1];
            out[2] = ((Long) in[2]).toString();
            out[3] = ((Long) in[3]).toString();
            ret.add(out);
        }
        return ret;
    }

    public List<String[]> getTopTenIPs() {
        return hdao.getTopTenIPs(counterId, timeIntervalToConstrain());
    }

    private String timeIntervalToConstrain() {
        if (interval == null) {
            interval = "today";
        }
        
        GregorianCalendar cal = new GregorianCalendar();
        switch (interval) {
            case "today":
                return Constants.DAY_FORMAT.format(cal.getTime()) + "%";
            case "yesterday":
                cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
                return Constants.DAY_FORMAT.format(cal.getTime()) + "%";
            case "cmonth":
                return Constants.MONTH_FORMAT.format(cal.getTime()) + "%";
            case "lmonth":
                cal.add(GregorianCalendar.MONTH, -1);
                return Constants.MONTH_FORMAT.format(cal.getTime()) + "%";
            case "cyear":
                return Constants.YEAR_FORMAT.format(cal.getTime()) + "%";
            case "lyear":
                cal.add(GregorianCalendar.YEAR, -1);
                return Constants.YEAR_FORMAT.format(cal.getTime()) + "%";
            case "all":
            default:
                return null;
        }
    }
}
