/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ridiculous.todomvc.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;

import ridiculous.todomvc.model.Todo;
import ridiculous.todomvc.repository.TodoRepository;

/**
 * A simple JDBC-based implementation of the {@link VetRepository} interface. Uses @Cacheable to cache the result of the
 * {@link findAll} method
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Thomas Risberg
 * @author Mark Fisher
 * @author Michael Isvy
 */
@Repository
public class JdbcTodoRepositoryImpl implements TodoRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTodoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Refresh the cache of Vets that the ClinicService is holding.
     *
     * @see org.springframework.samples.petclinic.model.service.ClinicService#findVets()
     */
    @Override
    public Collection<Todo> findAll() throws DataAccessException {
        List<Todo> vets = new ArrayList<Todo>();
        // Retrieve the list of all vets.
        vets.addAll(this.jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM vets ORDER BY last_name,first_name",
                ParameterizedBeanPropertyRowMapper.newInstance(Todo.class)));

        return vets;
    }
}
