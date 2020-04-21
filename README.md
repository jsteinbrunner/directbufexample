# directbufexample
Demostrates the direct memory buffer pool usage as head requests are made to S3 using both the synchronous and asynchrous S3 clients.

When the asynchronous client is used, the direct memory buffer pool continues to climb until a `Direct buffer memory` OutOfMemory exception occurs

When the synchronous client is used, the buff pool levels off and does not result in an `OutOfMemory` exception.

## Setup
In order to reproduce, run this project with the `-XX:MaxDirectMemorySize` JVM flag set.  The value of `-XX:MaxDirectMemorySize=100K` was used
to reproduce this error in approximately 6 requests.

## Test
### Async
Run the following curl command to reproduce the error
```
curl -X GET 'localhost:8090/headAsync'
```

After several requests, you will see a response that looks like the following:
```
{"timestamp":"2020-04-21T21:10:05.561+0000","status":500,"error":"Internal Server Error","message":"software.amazon.awssdk.core.exception.SdkClientException: Direct buffer memory","path":"/headAsync"}
```

### Sync
Run the following curl command to demonstrate a synhcronous request does not result in an error.
```
curl -X GET 'localhost:8090/headSync'
```
