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

import de.wpsverlinden.vcounter.entities.Referer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Stateless
public class RefererDAO {

    @PersistenceContext(unitName = "VCounterPU")
    private EntityManager em;

    public RefererDAO() {
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public Referer retrieveOrCreateReferer(String url) {
        Referer ref;
        TypedQuery<Referer> q = em.createQuery("SELECT re FROM Referer re WHERE re.url LIKE :url", Referer.class);
        q.setParameter("url", url);
        try {
            ref = (Referer)q.getSingleResult();
        } catch (NoResultException e) {
            ref = new Referer();
            ref.setUrl(url);
            em.persist(ref);
        }
        return ref;
    }
}
