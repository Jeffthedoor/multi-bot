// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.DriveRequests;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
    private Swerve swerve;

    public RobotContainer() {
        configureBindings();
    }

    private void initializeSubsystems() {
        swerve = Constants.TunerConstants.createDrivetrain();
    }

    private void configureBindings() {
        swerve.setDefaultCommand(swerve.applyRequest(DriveRequests.getDrive(null, null, null)));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
