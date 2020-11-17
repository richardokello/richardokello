/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.Optional;

import entities.TysncSettings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Tracom
 */

public interface TysncMstrSettingsRepo extends JpaRepository<TysncSettings, Long> {
    Optional <TysncSettings> findByParamname(String paramname);
}
