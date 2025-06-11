//generic class to handle alll inputs from DS

package frc.robot.utils;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.BooleanArraySubscriber;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;

public class InputInterface {
    private static Inputs inputs;
    private static NetworkTable table =  NetworkTableInstance.getDefault().getTable("Inputs");

    //server-side

    private static DoubleArrayPublisher sticksPublisher;
    private static BooleanArrayPublisher buttonsPublisher;
    private static BooleanPublisher isEnabledPublisher;
    private static DoublePublisher timeStampPublisher;

    public static void initializeServer(XboxController controller, BooleanSupplier isenabled, DoubleSupplier timeStamp) {
        inputs = new Inputs(controller, isenabled, timeStamp);
        publishInputs();
    }

    private static void publishInputs() {
        sticksPublisher = table.getDoubleArrayTopic("sticks").publish();

        buttonsPublisher = table.getBooleanArrayTopic("buttons").publish();
        isEnabledPublisher = table.getBooleanTopic("isEnabled").publish();
        timeStampPublisher = table.getDoubleTopic("timeStamp").publish();
    }

    public static void updateInputs(XboxController controller, BooleanSupplier isenabled, DoubleSupplier timeStamp) {
        inputs = new Inputs(controller, isenabled, timeStamp);
        
        sticksPublisher.accept(new double[] {
            inputs.leftX, inputs.leftY, inputs.rightX, inputs.rightY
        });
        buttonsPublisher.accept(new boolean[] {
            inputs.aButton, inputs.bButton, inputs.xButton, inputs.yButton,
            inputs.leftBumper, inputs.rightBumper, inputs.startButton, inputs.backButton
        });
        isEnabledPublisher.accept(inputs.isEnabled);
        timeStampPublisher.accept(inputs.timeStamp);
    }


    //client-side

    private static DoubleArraySubscriber sticksSubscriber;
    private static BooleanArraySubscriber buttonsSubscriber;
    private static BooleanSubscriber isEnabledSubscriber;
    private static DoubleSubscriber timeStampSubscriber;
    
    public static void initializeClient() {
        subscribeInputs();
    }

    private static void subscribeInputs() {
        sticksSubscriber = table.getDoubleArrayTopic("sticks").subscribe(new double[] {0, 0, 0, 0});
        buttonsSubscriber = table.getBooleanArrayTopic("buttons").subscribe(new boolean[] {false, false, false, false, false, false, false, false});
        isEnabledSubscriber = table.getBooleanTopic("isEnabled").subscribe(false);
        timeStampSubscriber = table.getDoubleTopic("timeStamp").subscribe(0.0);
    }

    public static Inputs grabInputs() {
        return new Inputs(
            sticksSubscriber.get(),
            buttonsSubscriber.get(),
            isEnabledSubscriber.get(),
            timeStampSubscriber.get()
        );
    }

    public static class Inputs {
        public double leftX;
        public double leftY;
        public double rightX;
        public double rightY;
        public boolean aButton;
        public boolean bButton;
        public boolean xButton;
        public boolean yButton;
        public boolean leftBumper;
        public boolean rightBumper;
        public boolean startButton;
        public boolean backButton;
        public boolean isEnabled;
        public double timeStamp;

        public Inputs(XboxController controller, BooleanSupplier isenabled, DoubleSupplier timeStamp) {
            this.isEnabled = isenabled.getAsBoolean();
            this.timeStamp = timeStamp.getAsDouble();
            this.leftX = controller.getLeftX();
            this.leftY = controller.getLeftY();
            this.rightX = controller.getRightX();
            this.rightY = controller.getRightY();
            this.aButton = controller.getAButton();
            this.bButton = controller.getBButton();
            this.xButton = controller.getXButton();
            this.yButton = controller.getYButton();
            this.leftBumper = controller.getLeftBumperButton();
            this.rightBumper = controller.getRightBumperButton();
            this.startButton = controller.getStartButton();
            this.backButton = controller.getBackButton();
        }

        public Inputs(double[] sticks, boolean[] buttons,
                      boolean isEnabled, double timeStamp) {
            this.leftX = sticks[0];
            this.leftY = sticks[1];
            this.rightX = sticks[2];
            this.rightY = sticks[3];

            this.aButton = buttons[0];
            this.bButton = buttons[1];
            this.xButton = buttons[2];
            this.yButton = buttons[3];
            this.leftBumper = buttons[4];
            this.rightBumper = buttons[5];
            this.startButton = buttons[6];
            this.backButton = buttons[7];
            this.isEnabled = isEnabled;
            this.timeStamp = timeStamp;
        }
    }
}
