import boto3
import os


def lambda_handler(event, context):
    COLLECTION_ID = os.environ["COLLECTION_ID"]
    S3OBJECT = {"Bucket": event["bucket"], "Name": event["key"]}
    client = boto3.client("rekognition")
    response = client.search_faces_by_image(
        CollectionId=COLLECTION_ID, Image={"S3Object": S3OBJECT}
    )
    if len(response["FaceMatches"]) == 0:
        return {"statusCode": 404, "error": "face doesn't match anyone!"}
    else:
        return {
            "statusCode": 200,
            "body": {
                "message": "found",
                "face_id": max(
                    response["FaceMatches"], key=lambda f: f["Face"]["Confidence"]
                )["Face"]["FaceId"],
            },
        }
