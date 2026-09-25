`GET /api/v2/ontologies/{ontology}/objectTypes/{objectType}/outgoingLinkTypes/{linkType}`

Get an outgoing link for an object type.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager** application.<br>示例: `Employee` |
| `linkType` | string | 是 | The API name of the outgoing link.<br>To find the API name for your link type, check the **Ontology Manager**.<br>示例: `directReport` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The Foundry branch to get the outgoing link types for an object type from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |

## Response

**LinkTypeSideV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `LinkTypeSideV2` | object | 是 | Success response.<br>示例: `{"apiName":"directReport","objectTypeApiName":"Employee","cardinality":"MANY"}` |
| `LinkTypeSideV2.apiName` | string | 是 | The name of the link type in the API. To find the API name for your Link Type, check the **Ontology Manager**<br>application. |
| `LinkTypeSideV2.displayName` | string | 是 | The display name of the entity. |
| `LinkTypeSideV2.status` | enum | 是 | The release status of the entity.<br>示例: `ACTIVE` |
| `LinkTypeSideV2.objectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `LinkTypeSideV2.cardinality` | enum | 是 | — |
| `LinkTypeSideV2.foreignKeyPropertyApiName` | string | 否 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LinkTypeSideV2.linkTypeRid` | string | 是 | — |

```json
{
  "apiName": "directReport",
  "objectTypeApiName": "Employee",
  "cardinality": "MANY"
}
```
