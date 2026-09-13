`GET /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/attachments/{property}/{attachmentRid}`

Get the metadata of a particular attachment in an attachment list.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |
| `primaryKey` | string | 是 | The primary key of the object containing the attachment.<br>示例: `50030` |
| `property` | string | 是 | The API name of the attachment property. To find the API name for your attachment,<br>check the **Ontology Manager** or use the **Get object type** endpoint.<br>示例: `performance` |
| `attachmentRid` | string | 是 | The RID of the attachment.<br>示例: `ri.attachments.main.attachment.bb32154e-e043-4b00-9461-93136ca96b6f` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to read from. If not specified, the default branch will be used.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |

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
