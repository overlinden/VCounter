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

import de.wpsverlinden.vcounter.Constants;
import de.wpsverlinden.vcounter.entities.Stat;
import de.wpsverlinden.vcounter.entities.StatCompositeKey;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Stateless
public class StatDAO {

    @PersistenceContext(unitName = "IPCounterPU")
    private EntityManager em;

    public StatDAO() {
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void registerStatFor(long counterId, boolean isNewVisitor) {
        String dayString = Constants.DAY_FORMAT.format(new GregorianCalendar().getTime());
        StatCompositeKey key = new StatCompositeKey();
        key.setCounterId(counterId);
        key.setDay(dayString);
        Stat ds = em.find(Stat.class, key);
        if (ds == null) {
            ds = new Stat();
            ds.setCounterId(counterId);
            ds.setDay(dayString);
            ds.setHitCount(1);
            ds.setVisitorCount(1);
        } else {
            if (isNewVisitor) {
                ds.setVisitorCount(ds.getVisitorCount() + 1);
            }
            ds.setHitCount(ds.getHitCount() + 1);
        }
        em.merge(ds);
    }

    public Stat getAggregatedDataFor(long counterId, String pattern) {
        Stat s = new Stat();
        s.setCounterId(counterId);
        s.setDay(pattern);
        Query q = em.createQuery("SELECT SUM(s.hitCount), SUM(s.visitorCount) FROM Stat s WHERE s.counterId = :counterId AND s.day LIKE :pattern GROUP BY s.counterId");
        q.setParameter("pattern", pattern + "%");
        q.setParameter("counterId", counterId);

        try {
            Object[] obj = (Object[]) q.getSingleResult();
            Long hits = ((Number) obj[0]).longValue();
            Long visits = ((Number) obj[1]).longValue();
            s.setHitCount(hits);
            s.setVisitorCount(visits);
        } catch (NoResultException e) {
            //Nothing to do. This exception may occur because this function iss called for all time intervals
        }
        return s;
    }

    public List<Stat> getLast30DataFor(long counterId) {
        TypedQuery<Stat> q = em.createQuery("SELECT s FROM Stat s WHERE s.counterId = :counterId ORDER BY s.day DESC", Stat.class);
        q.setParameter("counterId", counterId);
        q.setMaxResults(30);
        return q.getResultList();
    }

    public List<Stat> getTopTenHits(long counterId, String interval) {
        TypedQuery<Stat> q;
        if (interval == null) {
            q = em.createQuery("SELECT s FROM Stat s WHERE s.counterId = :counterId ORDER BY s.hitCount DESC", Stat.class);
        } else {
            q = em.createQuery("SELECT s FROM Stat s WHERE s.counterId = :counterId AND s.day LIKE :interval ORDER BY s.hitCount DESC", Stat.class);
            q.setParameter("interval", interval);
        }
        q.setParameter("counterId", counterId);
        q.setMaxResults(10);
        return q.getResultList();
    }

    public List<Stat> getTopTenVisits(long counterId, String interval) {
        TypedQuery<Stat> q;
        if (interval == null) {
            q = em.createQuery("SELECT s FROM Stat s WHERE s.counterId = :counterId ORDER BY s.visitorCount DESC", Stat.class);
        } else {
            q = em.createQuery("SELECT s FROM Stat s WHERE s.counterId = :counterId AND s.day LIKE :interval ORDER BY s.visitorCount DESC", Stat.class);
            q.setParameter("interval", interval);
        }
        q.setParameter("counterId", counterId);
        q.setMaxResults(10);
        return q.getResultList();
    }
}
