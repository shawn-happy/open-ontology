`GET /api/v2/ontologies/attachments/{attachmentRid}`

Get the metadata of an attachment.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `attachmentRid` | string | 是 | The RID of the attachment.<br>示例: `ri.attachments.main.attachment.bb32154e-e043-4b00-9461-93136ca96b6f` |

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
