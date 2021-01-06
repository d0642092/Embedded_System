import RPi.GPIO as GPIO
import time

ledPin = 12
def setup():
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(ledPin,GPIO.OUT)

def loop():
    while True:
        GPIO.output(ledPin,GPIO.HIGH)
        time.sleep(1)
        GPIO.output(ledPin,GPIO.low)
        time.sleep(1)

def destroy():
    GPIO.output(ledPin,GPIO.LOW)
    GPIO.cleanup()
