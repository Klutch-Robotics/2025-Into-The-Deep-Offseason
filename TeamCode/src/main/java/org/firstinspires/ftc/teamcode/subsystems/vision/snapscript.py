import cv2
import numpy as np

def get_orientation(rect):
    width, height = rect[1]
    angle = rect[2]

    if width < height:
        angle = angle + 90  # make positive
    # else:
    #     angle = angle  # rotate base

    # angle = angle % 180  # ensure within 0–180
    # angle = angle - 90
    return angle

# runPipeline() is called every frame by Limelight's backend.
def runPipeline(image, llrobot):
    # convert the input image to the HSV color space
    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    # convert the hsv to a binary image by removing any pixels
    # that do not fall within the following HSV Min/Max values
    img_threshold = cv2.inRange(img_hsv, (0, 229, 35), (33, 255, 178))

    # find contours in the new binary image
    contours, _ = cv2.findContours(img_threshold,
    cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    largestContour = np.array([[]])

    # initialize an empty array of values to send back to the robot
    llpython = [0,0,0,0,0,0,0,0]

    # if contours have been detected, draw them
    if len(contours) > 0:
        cv2.drawContours(image, contours, -1, 255, 2)
        # record the largest contour
        largestContour = max(contours, key=cv2.contourArea)

        rect = cv2.minAreaRect(largestContour)

        angle = get_orientation(rect)

        # print(angle)

        # get the unrotated bounding box that surrounds the contour
        x,y,w,h = cv2.boundingRect(largestContour)

        # Calculate tx and ty
        center_x = x + w/2
        center_y = y + h/2
        tx = (center_x - image.shape[1]/2) / (image.shape[1]/2)  # Normalize to [-1, 1]
        ty = (center_y - image.shape[0]/2) / (image.shape[0]/2)  # Normalize to [-1, 1]

        # draw the unrotated bounding box
        cv2.rectangle(image,(x,y),(x+w,y+h),(0,255,255),2)

        # record some custom data to send back to the robot
        width, height = rect[1]
        if (width * height < 30000):
            llpython = [angle,tx,ty,1,width * height,h,0,7]
            print("rejected")
        else:
            llpython = [angle,tx,ty,0,width * height,h,1,7]

    #return the largest contour for the LL crosshair, the modified image, and custom robot data
    return largestContour, image, llpython