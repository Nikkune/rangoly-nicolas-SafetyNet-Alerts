
# SafetyNet Alerts

The SafetyNet Alerts application sends information to emergency service systems. This project involves developing the backend, an API that returns various information based on requests.
## API Reference

### Get all residents covered by a fire station

```
  GET /firestation?stationNumber=${station_number}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `station_number` | `number` | **Required**. The number of the fire station |

### Get all children at the given address

```
  GET /childAlert?address=${address}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `address`      | `string` | **Required**. Address of the house to check |

### Get all phone numbers of residents covered by a fire station

```
  GET /phoneAlert?firestation=${firestation_number}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `firestation_number` | `number` | **Required**. The number of the fire station |

### Get all residents at a given address and the fire station covering them

```
  GET /fire?address=${address}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `address`      | `string` | **Required**. Address to check |

### Get all households covered by a list of fire stations

```
  GET /flood/stations?stations=${stations}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `stations`      | `array<number>` | **Required**. List of station numbers |

### Get information about residents by last name

```
  GET /personInfo?lastName=${lastName}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `lastName`      | `string` | **Required**. Last name to search |

### Get email addresses of all residents in a city

```
  GET /communityEmail?city=${city}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `city`      | `string` | **Required**. City to search |

