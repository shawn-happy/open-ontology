`POST /api/v2/ontologies/attachments/upload`

Upload an attachment to use in an action. Any attachment which has not been linked to an object via
an action within one hour after upload will be removed.
Previously mapped attachments which are not connected to any object anymore are also removed on
a biweekly basis.
The body of the request must contain the binary content of the file and the `Content-Type` header must be `application/octet-stream`.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-write`.

**OAuth2 scopes**: `api:ontologies-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `filename` | string | 是 | The name of the file being uploaded.<br>示例: `My Image.jpeg` |

## Request body

## Response

**AttachmentV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AttachmentV2` | object | 是 | Success response.<br>示例: `{"rid":"ri.attachments.main.attachment.bb32154e-e043-4b00-9461-93136ca96b6f","filename":"My Image.jpeg","sizeBytes":393469,"mediaType":"image/jpeg"}` |
| `AttachmentV2.rid` | string | 是 | The unique resource identifier of an attachment. |
| `AttachmentV2.filename` | string | 是 | The name of a File within Foundry. Examples: `my-file.txt`, `my-file.jpg`, `dataframe.snappy.parquet`.<br>示例: `my-file.csv` |
| `AttachmentV2.sizeBytes` | string | 是 | The size of the file or attachment in bytes.<br>示例: `72526847` |
| `AttachmentV2.mediaType` | string | 是 | The [media type](https://www.iana.org/assignments/media-types/media-types.xhtml) of the file or attachment.<br>Examples: `application/json`, `application/pdf`, `application/octet-stream`, `image/jpeg`<br>示例: `application/pdf` |

```json
{
  "rid": "ri.attachments.main.attachment.bb32154e-e043-4b00-9461-93136ca96b6f",
  "filename": "My Image.jpeg",
  "sizeBytes": 393469,
  "mediaType": "image/jpeg"
}
```
