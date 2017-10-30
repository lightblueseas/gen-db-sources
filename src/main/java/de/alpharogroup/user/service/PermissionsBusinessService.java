package de.alpharogroup.user.service;

import de.alpharogroup.db.service.AbstractBusinessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.alpharogroup.user.repositories.PermissionsRepository;
import de.alpharogroup.user.entities.Permissions;
import de.alpharogroup.user.service.PermissionsService;

/**
 * The service class {@link PermissionsBusinessService}.
 */
@Transactional
@Service("permissionsService")
public class PermissionsBusinessService 
	extends
		AbstractBusinessService<Permissions, java.lang.Integer, PermissionsRepository> 
	implements 
		PermissionsService
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Sets the specific repository.
	 *
	 * @param repository the repository
	 */
	@Autowired
	public void setPermissionsRepository(PermissionsRepository repository) {
		setRepository(repository);
	}

}