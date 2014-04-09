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
import de.wpsverlinden.vcounter.entities.TopTenDTO;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@ManagedBean
public class DetailStatBean {

    @Inject
    private HitDAO hdao;

    @Inject
    private StatDAO sdao;

    @ManagedProperty(value = "#{param.id}")
    private long counterId;
    
    @ManagedProperty(value = "#{param.t}")
    private String interval;
    
    private List<Stat> last30, topTenHits, topTenVisits;
    private List<TopTenDTO> topTenIPs, topTenUserAgents;
    private List<String[]> topTenReferers;

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
        if (last30 == null) {
            last30 = sdao.getLast30DataFor(counterId);
            for (Stat s : last30) {
                try {
                    s.setDay(Constants.DAY_FORMAT_DEU.format(Constants.DAY_FORMAT.parse(s.getDay())));
                } catch (ParseException ex) {
                }
            }
        }
        return last30;
    }

    public List<Stat> getTopTenHits() {
        if (topTenHits == null) {
            topTenHits = sdao.getTopTenHits(counterId, timeIntervalToConstrain());
            for (Stat s : topTenHits) {
                try {
                    s.setDay(Constants.DAY_FORMAT_DEU.format(Constants.DAY_FORMAT.parse(s.getDay())));
                } catch (ParseException ex) {
                }
            }
        }
        return topTenHits;
    }

    public List<Stat> getTopTenVisits() {
        if (topTenVisits == null) {
            topTenVisits = sdao.getTopTenVisits(counterId, timeIntervalToConstrain());
            for (Stat s : topTenVisits) {
                try {
                    s.setDay(Constants.DAY_FORMAT_DEU.format(Constants.DAY_FORMAT.parse(s.getDay())));
                } catch (ParseException ex) {
                }
            }
        }
        return topTenVisits;
    }

    public List<TopTenDTO> getTopTenUserAgents() {
        if (topTenUserAgents == null) {
            topTenUserAgents = hdao.getTopTenUserAgents(counterId, timeIntervalToConstrain());
        }
        return topTenUserAgents;
    }

    public List<String[]> getTopTenReferers() {
        if (topTenReferers == null) {
            List<TopTenDTO> refs = hdao.getTopTenReferers(counterId, timeIntervalToConstrain());
            topTenReferers = new LinkedList<>();
            for (TopTenDTO in : refs) {
                String[] out = new String[4];
                if (in.getName().length() > 60) {
                    out[0] = (in.getName()).substring(0, 59).concat("...");
                } else {
                    out[0] = in.getName();
                }
                out[1] = in.getName();
                out[2] = String.valueOf(in.getVisits());
                out[3] = String.valueOf(in.getHits());
                topTenReferers.add(out);
            }
        }
        return topTenReferers;
    }

    public List<TopTenDTO> getTopTenIPs() {
        if (topTenIPs == null) {
            topTenIPs = hdao.getTopTenIPs(counterId, timeIntervalToConstrain());
        }
        return topTenIPs;
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
