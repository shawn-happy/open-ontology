`POST /api/v2/ontologies/{ontology}/queries/{queryApiName}/execute`

Executes a Query using the given parameters. By default, the latest version of the Query is executed. 
The latest version is the one that was most recently published, which may be a pre-release version.

Optional parameters do not need to be supplied.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `queryApiName` | string | 是 | The API name of the Query to execute.<br>示例: `getEmployeesInCity` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `version` | string | 否 | The semantic version range of the Query to execute. Function execution selects a satisfying version from<br>this range. If not specified, the version most recently published is used. |
| `branch` | string | 否 | The Foundry branch to execute the query from. If not specified, the default branch is used.<br>Branches are an experimental feature and not all workflows are supported.<br>When provided without `version`, the latest version on this branch is used, including pre-release versions.<br>When provided with `version`, the specified version must exist on the branch.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `transactionId` | string | 否 | The ID of an Ontology transaction to read from.<br>Transactions are an experimental feature and all workflows may not be supported. |
| `scenarioRid` | string | 否 | The resource identifier of an ontology scenario to execute the query on.<br>示例: `ri.actions..scenario.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

## Request body

```json
{
  "parameters": {
    "city": "New York"
  }
}
```

## Response

**ExecuteQueryResponse**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ExecuteQueryResponse` | object | 是 | Success response.<br>示例: `{"value":["EMP546","EMP609","EMP989"]}` |
| `ExecuteQueryResponse.value` | any | 是 | Represents the value of data in the following format. Note that these values can be nested, for example an array of structs.<br>\| Type                                \| JSON encoding                                         \| Example                                                                                                                                                       \|<br>\|-------------------------------------\|-------------------------------------------------------\|---------------------------------------------------------------------------------------------------------------------------------------------------------------\|<br>\| Array                               \| array                                                 \| `["alpha", "bravo", "charlie"]`                                                                                                                               \|<br>\| Attachment                          \| string                                                \| `"ri.attachments.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"`                                                                                       \|<br>\| Boolean                             \| boolean                                               \| `true`                                                                                                                                                        \|<br>\| Byte                                \| number                                                \| `31`                                                                                                                                                          \|<br>\| CipherText                          \| string                                                \| `"CIPHER::ri.bellaso.main.cipher-channel.e414ab9e-b606-499a-a0e1-844fa296ba7e::unzjs3VifsTxuIpf1fH1CJ7OaPBr2bzMMdozPaZJtCii8vVG60yXIEmzoOJaEl9mfFFe::CIPHER"` \|<br>\| Date                                \| ISO 8601 extended local date string                   \| `"2021-05-01"`                                                                                                                                                \|<br>\| Decimal                             \| string                                                \| `"2.718281828"`                                                                                                                                               \|<br>\| Double                              \| number                                                \| `3.14159265`                                                                                                                                                  \|<br>\| EntrySet                            \| array of JSON objects                                 \| `[{"key": "EMP1234", "value": "true"}, {"key": "EMP4444", "value": "false"}]`                                                                                 \|<br>\| Float                               \| number                                                \| `3.14159265`                                                                                                                                                  \|<br>\| Integer                             \| number                                                \| `238940`                                                                                                                                                      \|<br>\| Long                                \| string                                                \| `"58319870951433"`                                                                                                                                            \|<br>\| Marking                             \| string                                                \| `"MU"`                                                                                                                                                        \|<br>\| Null                                \| null                                                  \| `null`                                                                                                                                                        \|<br>\| Object Set                          \| string OR the object set definition                   \| `ri.object-set.main.versioned-object-set.h13274m8-23f5-431c-8aee-a4554157c57z`                                                                                \|<br>\| Ontology Object Reference           \| JSON encoding of the object's primary key             \| `10033123` or `"EMP1234"`                                                                                                                                     \|<br>\| Ontology Interface Object Reference \| JSON encoding of the object's API name and primary key\| `{"objectTypeApiName":"Employee", "primaryKeyValue":"EMP1234"}`                                                                                               \|<br>\| Ontology Object Type Reference      \| string of the object type's api name                  \| `"Employee"`                                                                                                                                                  \|<br>\| Scenario Reference                  \| string of the scenario RID                            \| `"ri.actions..scenario.cf2a8a49-8b56-446d-ab04-a6bc7fadef48"`                                                                                                 \|<br>\| Set                                 \| array                                                 \| `["alpha", "bravo", "charlie"]`                                                                                                                               \|<br>\| Short                               \| number                                                \| `8739`                                                                                                                                                        \|<br>\| String                              \| string                                                \| `"Call me Ishmael"`                                                                                                                                           \|<br>\| Struct                              \| JSON object                                           \| `{"name": "John Doe", "age": 42}`                                                                                                                             \|<br>\| TwoDimensionalAggregation           \| JSON object                                           \| `{"groups": [{"key": "alpha", "value": 100}, {"key": "beta", "value": 101}]}`                                                                                 \|<br>\| ThreeDimensionalAggregation         \| JSON object                                           \| `{"groups": [{"key": "NYC", "groups": [{"key": "Engineer", "value" : 100}]}]}`                                                                                \|<br>\| Timestamp                           \| ISO 8601 extended offset date-time string in UTC zone \| `"2021-01-04T05:00:00Z"`                                                                                                                                      \| |

```json
{
  "value": [
    "EMP546",
    "EMP609",
    "EMP989"
  ]
}
```
