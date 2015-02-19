/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.forge.camel.commands.project;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.maven.archetype.catalog.ArchetypeCatalog;
import org.apache.maven.archetype.catalog.io.xpp3.ArchetypeCatalogXpp3Reader;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.maven.archetype.ArchetypeCatalogFactory;
import org.jboss.forge.addon.maven.archetype.ArchetypeCatalogFactoryRegistry;
import org.jboss.forge.addon.parser.xml.resources.XMLResource;
import org.jboss.forge.furnace.container.cdi.events.Local;
import org.jboss.forge.furnace.event.PostStartup;
import org.jboss.forge.furnace.services.Imported;

/**
 * The Fabric8 archetypes
 */
public class FabricArchetypeCatalogFactory implements ArchetypeCatalogFactory {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final String name = "fabric8";

    @Inject
    Imported<DependencyResolver> resolver;

    private ArchetypeCatalog cachedArchetypes;

    void startup(@Observes @Local PostStartup startup, ArchetypeCatalogFactoryRegistry registry) {
        // must use this to trigger startup event so we can add ourselves
        if (registry.getArchetypeCatalogFactory(name) == null) {
            registry.addArchetypeCatalogFactory(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArchetypeCatalog getArchetypeCatalog() {
       if (cachedArchetypes == null) {
            // TODO: version should be not hardcoded
            // use the camel catalog to load the archetypes
            Coordinate coordinate = CoordinateBuilder.create()
                    .setGroupId("io.fabric8.archetypes")
                    .setArtifactId("archetypes-catalog")
                    .setVersion("2.2-SNAPSHOT")
                    .setClassifier("archetype-catalog")
                    .setPackaging("xml");

            Dependency dependency = resolver.get().resolveArtifact(DependencyQueryBuilder.create(coordinate));
            if (dependency != null) {
                XMLResource xml = (XMLResource) dependency.getArtifact();
                if (xml != null) {
                    try (InputStream is = xml.getResourceInputStream()) {
                        cachedArchetypes = new ArchetypeCatalogXpp3Reader().read(is);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error while retrieving archetypes", e);
                    }
                }
            }
        }
        return cachedArchetypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FabricArchetypeCatalogFactory that = (FabricArchetypeCatalogFactory) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}