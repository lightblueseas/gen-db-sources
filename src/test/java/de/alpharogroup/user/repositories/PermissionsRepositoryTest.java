package de.alpharogroup.user.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import de.alpharogroup.user.entities.Permissions;

@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
public class PermissionsRepositoryTest extends AbstractTestNGSpringContextTests 
{

    @Autowired
	private PermissionsRepository permissionsRepository;

	@Test
	public void testFindAll() {
		List<Permissions> list = permissionsRepository.findAll();
	}

}
