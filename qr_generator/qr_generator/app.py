import random
import string
import boto3
import io
import os
from CuteR import CuteR


def lambda_handler(event, context):
    # debug dumps
    print(event)
    print(os.environ["BUCKET"])
    # generate oka special qr code, else normal qr code
    if event.get("mode") == "oka":
        output = CuteR.produce(
            event.get("secret"), "oka.png", colourful=(event.get("colorful", False))
        )
    else:
        output = CuteR.produce(event.get("secret"), "white.png")
    f = io.BytesIO()
    output[0].save(f, format="png")
    # rewind the file
    f.seek(0)
    # save to s3
    s3 = boto3.resource("s3")
    filename = event.get("key")
    s3.meta.client.upload_fileobj(
        f,
        os.environ["BUCKET"],
        filename,
        ExtraArgs={"ACL": "public-read", "ContentType": "image/png"},
    )

    # get the url of the object we just uploaded
    bucket_location = s3.meta.client.get_bucket_location(Bucket=os.environ["BUCKET"])
    url = "https://s3.{0}.amazonaws.com/{1}/{2}".format(
        bucket_location["LocationConstraint"], os.environ["BUCKET"], filename,
    )
    # debug dumps
    print(url)
    return {
        "statusCode": 200,
        "body": url,
    }


def randomStringDigits(stringLength=6):
    """Generate a random string of letters and digits """
    lettersAndDigits = string.ascii_letters + string.digits
    return "".join(random.choice(lettersAndDigits) for i in range(stringLength))
