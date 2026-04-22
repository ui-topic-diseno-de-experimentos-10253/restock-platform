package com.restock.platform.iam.domain.model.commands;

import com.restock.platform.iam.domain.model.entities.Role;

import java.util.List;

/**
 * Sign up command
 * <p>
 *     This class represents the command to sign up a user.
 * </p>
 * @param username the username of the user
 * @param password the password of the user
 * @param roleId the id of role of the user
 *
 */
public record SignUpCommand(String username, String password,  Long roleId) {
}