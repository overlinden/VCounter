/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wpsverlinden.vcounter.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Oliver
 */
@Entity
public class TopTenDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String name;
    
    private int visits;
    private int hits;

    public TopTenDTO() {
    }

    public TopTenDTO(String name, Long visits, Long hits) {
        this.name = name;
        this.visits = visits.intValue();
        this.hits = hits.intValue();
    }

    public String getName() {
        return name;
    }

    public int getVisits() {
        return visits;
    }

    public int getHits() {
        return hits;
    }
}
