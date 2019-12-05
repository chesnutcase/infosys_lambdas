import boto3
import os


def lambda_handler(event, context):
    COLLECTION_ID = os.environ["COLLECTION_ID"]
    S3OBJECT = {"Bucket": event["bucket"], "Name": event["key"]}
    client = boto3.client("rekognition")
    response = client.detect_faces(Image={"S3Object": S3OBJECT})
    number_of_faces_detected = len(response["FaceDetails"])
    if number_of_faces_detected == 0:
        return {"statusCode": 422, "body": {"error": "no faces found"}}
    elif number_of_faces_detected > 1:
        return {
            "statusCode": 422,
            "body": {"error": f"expected 1 face, got {number_of_faces_detected} faces"},
        }
    while True:
        try:
            response = client.index_faces(
                CollectionId=COLLECTION_ID, Image={"S3Object": S3OBJECT}
            )
            break
        except Exception as ex:
            if "ResourceNotFoundException" in str(type(ex)):
                client.create_collection(CollectionId=COLLECTION_ID)
            else:
                return {"statusCode": 500, "body": {"error": str(ex)}}

    if len(response["UnindexedFaces"]) > 0:
        return {
            "statusCode": 422,
            "body": {
                "error": "bad face",
                "reasons": response["UnindexedFaces"]["Reasons"],
            },
        }
    else:
        return {
            "statusCode": 200,
            "body": {
                "message": "ok",
                "face_id": response["FaceRecords"][0]["Face"]["FaceId"],
            },
        }
