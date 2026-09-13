`GET /api/v2/ontologies/{ontology}`

Gets a specific ontology for a given Ontology API name or RID.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |

## Response

**OntologyV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `OntologyV2` | object | 是 | Success response.<br>示例: `{"apiName":"default-ontology","displayName":"Ontology","description":"The default ontology","rid":"ri.ontology.main.ontology.c61d9ab5-2919-4127-a0a1-ac64c0ce6367"}` |
| `OntologyV2.apiName` | string | 是 | — |
| `OntologyV2.displayName` | string | 是 | The display name of the entity. |
| `OntologyV2.description` | string | 是 | — |
| `OntologyV2.rid` | string | 是 | The unique Resource Identifier (RID) of the Ontology. To look up your Ontology RID, please use the<br>`List ontologies` endpoint or check the **Ontology Manager**. |

```json
{
  "apiName": "default-ontology",
  "displayName": "Ontology",
  "description": "The default ontology",
  "rid": "ri.ontology.main.ontology.c61d9ab5-2919-4127-a0a1-ac64c0ce6367"
}
```
