package de.alpharogroup.user.repositories;

import org.springframework.stereotype.Repository;

import de.alpharogroup.db.repository.AbstractRepository;

import de.alpharogroup.user.entities.Permissions;

@Repository("permissionsRepository")
public class PermissionsRepository extends AbstractRepository<Permissions, java.lang.Integer> 
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
}
