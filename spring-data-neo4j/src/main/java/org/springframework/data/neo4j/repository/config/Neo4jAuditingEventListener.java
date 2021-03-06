/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 *  conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.springframework.data.neo4j.repository.config;

import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListener;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Frantisek Hartman
 */
public class Neo4jAuditingEventListener extends EventListenerAdapter implements EventListener {

	private final ObjectFactory<IsNewAwareAuditingHandler> auditingHandlerFactory;

	/**
	 * Creates a new {@link Neo4jAuditingEventListener} using the given {@link MappingContext} and {@link AuditingHandler}
	 * provided by the given {@link ObjectFactory}.
	 *
	 * @param auditingHandlerFactory must not be {@literal null}.
	 */
	public Neo4jAuditingEventListener(ObjectFactory<IsNewAwareAuditingHandler> auditingHandlerFactory,
									  SessionFactory sessionFactory) {

		Assert.notNull(auditingHandlerFactory, "IsNewAwareAuditingHandler must not be null!");
		this.auditingHandlerFactory = auditingHandlerFactory;

		sessionFactory.register(this);
	}

	@Override
	public void onPreSave(Event event) {
		Object object = event.getObject();
		if (object != null) {
			auditingHandlerFactory.getObject().markAudited(object);
		}
	}

}
