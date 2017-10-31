package de.alpharogroup.questionaries.system.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import de.alpharogroup.db.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
public class Questions extends BaseEntity<Integer>
{
	private static final long serialVersionUID = 1L;
	private String foo;

}
