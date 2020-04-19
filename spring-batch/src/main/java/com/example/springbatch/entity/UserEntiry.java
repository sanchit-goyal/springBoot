package com.example.springbatch.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class UserEntiry {

	@Id
	private Long id;

	private String firstName;

	private String lastName;

	private String age;

}
