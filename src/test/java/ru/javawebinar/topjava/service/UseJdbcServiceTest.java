package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "jdbc")
public class UseJdbcServiceTest extends UserServiceTest {
}