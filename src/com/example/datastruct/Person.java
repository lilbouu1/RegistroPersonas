package com.example.datastruct;

public class Person {
	private int id;
    private String name;
    private int age;
    private String gender;
    private String address;

    public Person(int id, String name, int age, String gender, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
    }

    public int getId() { 
    	return id;
    	}
    public String getName() { 
    	return name; 
    	}
    public int getAge() { 
    	return age; 
    	}
    public String getGender() { 
    	return gender; 
    	}
    public String getAddress() { 
    	return address;
    	}

    public void setName(String name) { 
    	this.name = name; 
    	}
    public void setAge(int age) { 
    	this.age = age;
    	}
    public void setGender(String gender) { 
    	this.gender = gender;
    	}
    public void setAddress(String address) { 
    	this.address = address;
    	}

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + name + " | Edad: " + age
                + " | Género: " + gender + " | Dirección: " + address;
    }
}
