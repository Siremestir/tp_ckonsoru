/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao;

import com.fges.ckonsoru.model.Disponibilite;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author julie.jacques
 */
public interface DisponibilitesDAO {
    
    public List<Disponibilite> getDisponibilitesPourDate(LocalDate date);

}
