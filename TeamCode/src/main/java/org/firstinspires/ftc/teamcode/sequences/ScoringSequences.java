package org.firstinspires.ftc.teamcode.sequences;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.teamcode.subsystems.*;

public class ScoringSequences {
    private final Shooter shooter;
    private final Intake intake;
    private final DrivingSequences driveSequences;

    public ScoringSequences(Shooter shooter, Intake intake, DrivingSequences driveSequences) {

        this.intake = intake;
        this.shooter = shooter;
        this.driveSequences = driveSequences;
    }

    /**
     * Action to score a ball
     */
    public Action score(Pose2d robotPos, Pose2d targetPos) {
        /*
         *  This will be a sequential action consisting of:
         *  Getting the ball into position
         *  Setting the velocity of the flywheel - need something like shooter.getVInitial();
         *  Then shooting the ball
         */
        double velocity = shooter.calcVelocity(robotPos, targetPos);
        return new SequentialAction(
                shooter.powerFly(velocity),
                intake.eject(),
                intake.afterEject(),
                shooter.powerDownFly()
        );
    }

    // TODO: Make an action to score 3 balls also

    // TODO: Make an action that drives and intakes at the same time
    public Action intake(String name) {
        intake.intakeOn();
        return new RaceAction(
            driveSequences.goToLinearSlow(name),
            new SequentialAction(
                intake.monitorIntake()
//                intake.intakeBall(intake.sensing.getColor()), TODO
//                intake.monitorIntake(),
//                intake.intakeBall(intake.sensing.getColor()),
//                intake.monitorIntake(),
//                intake.intakeBall(intake.sensing.getColor())
            )

        );
    }

    /**
     * Example sequential action
     *
     * @return a sequence of actions
     */
    public Action build() {
        return new SequentialAction(
                shooter.powerFly(1.0)
        );
    }

}
