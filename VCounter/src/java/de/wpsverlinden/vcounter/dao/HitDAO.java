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
package de.wpsverlinden.vcounter.dao;

import de.wpsverlinden.vcounter.entities.TopTenDTO;
import de.wpsverlinden.vcounter.Constants;
import de.wpsverlinden.vcounter.entities.*;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Stateless
public class HitDAO {

    @PersistenceContext(unitName = "VCounterPU")
    private EntityManager em;

    @Inject
    private UserAgentDAO uadao;

    @Inject
    private RefererDAO refdao;

    public HitDAO() {
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void registerHitFor(long counterId, HttpSession session, HttpServletRequest request) {
        Hit v = new Hit();
        v.setCounterId(counterId);
        v.setSessionId(session.getId());
        String[] ipSplit = request.getHeader("x-forwarded-for").split("\\.");
        v.setIP(ipSplit[0] + "." + ipSplit[1] + ".0.0");
        UserAgent ua = uadao.retrieveOrCreateUserAgent(request.getHeader("user-agent") != null ? request.getHeader("user-agent") : "unbekannt");
        v.setUserAgent(ua);
        Referer ref = refdao.retrieveOrCreateReferer(request.getHeader("referer") != null ? request.getHeader("referer") : "unbekannt");
        v.setReferer(ref);
        v.setTimestamp(Constants.TIME_FORMAT.format(new GregorianCalendar().getTime()));
        em.persist(v);
    }

    public int getOnlineUsers(long counterId, String startTS) {
        TypedQuery<Long> q = em.createQuery("SELECT count(distinct h.sessionId) FROM Hit h WHERE h.counterId = :counterId AND h.timestamp >= :startTS", Long.class);
        q.setParameter("counterId", counterId);
        q.setParameter("startTS", startTS);
        return (q.getSingleResult()).intValue();
    }

    public List<Hit> getLastUsers(long counterId) {
        TypedQuery<Hit> q = em.createQuery("SELECT h FROM Hit h WHERE h.counterId = :counterId ORDER BY h.timestamp DESC", Hit.class);
        q.setParameter("counterId", counterId);
        q.setMaxResults(5);
        return q.getResultList();
    }

    public List<TopTenDTO> getTopTenUserAgents(long counterId, String interval) {
        Query q;
        if (interval == null) {
            q = em.createQuery("SELECT NEW TopTenDTO(h.userAgent.name, count(distinct h.sessionId), count(*)) FROM Hit h WHERE h.counterId = :counterId GROUP BY h.userAgent ORDER BY count(distinct h.sessionId) DESC");
        } else {
            q = em.createQuery("SELECT NEW TopTenDTO(h.userAgent.name, count(distinct h.sessionId), count(*)) FROM Hit h WHERE h.counterId = :counterId AND h.timestamp LIKE :interval GROUP BY h.userAgent ORDER BY count(distinct h.sessionId) DESC");
            q.setParameter("interval", interval);
        }
        q.setParameter("counterId", counterId);
        q.setMaxResults(10);
        return q.getResultList();
    }

    public List<TopTenDTO> getTopTenReferers(long counterId, String interval) {
        Query q;
        if (interval == null) {
            q = em.createQuery("SELECT NEW TopTenDTO(h.referer.url, count(distinct h.sessionId), count(*)) FROM Hit h WHERE h.counterId = :counterId GROUP BY h.referer ORDER BY count(distinct h.sessionId) DESC");
        } else {
            q = em.createQuery("SELECT NEW TopTenDTO(h.referer.url, count(distinct h.sessionId), count(*)) FROM Hit h WHERE h.counterId = :counterId AND h.timestamp LIKE :interval GROUP BY h.referer ORDER BY count(distinct h.sessionId) DESC");
            q.setParameter("interval", interval);
        }
        q.setParameter("counterId", counterId);
        q.setMaxResults(10);
        return q.getResultList();
    }

    public List<TopTenDTO> getTopTenIPs(long counterId, String interval) {
        Query q;
        if (interval == null) {
            q = em.createQuery("SELECT NEW TopTenDTO(h.IP, count(distinct h.sessionId), count(*)) FROM Hit h WHERE h.counterId = :counterId GROUP BY h.IP ORDER BY count(distinct h.sessionId) DESC");
        } else {
            q = em.createQuery("SELECT NEW TopTenDTO(h.IP, count(distinct h.sessionId), count(*)) FROM Hit h WHERE h.counterId = :counterId AND h.timestamp LIKE :interval GROUP BY h.IP ORDER BY count(distinct h.sessionId) DESC");
            q.setParameter("interval", interval);
        }
        q.setParameter("counterId", counterId);
        q.setMaxResults(10);
        return q.getResultList();
    }
}
