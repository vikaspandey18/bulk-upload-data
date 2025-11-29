# 📥 Excel Bulk Upload API (Spring Boot)

This project provides a REST API to upload Excel files and bulk-insert data into the database.  
It uses **Spring Boot**, **Apache POI**, **MapStruct**, and **Spring Data JPA**.

---

## 🚀 Features
- Upload Excel (.xlsx/.xls) file  
- Parse Excel rows using Apache POI  
- Map DTO ↔ Entity with MapStruct  
- Bulk insert using JPA `saveAll()`  
- Clean success/error responses  
- Transactional import handling  

---

## 📌 API Endpoint

### **POST /api/createSmaLead**

Uploads an Excel file and imports its rows into the database.

#### Request Format

```
com.example.bulkupload
│
├── controller
│   └── SmaLeadController.java
│
├── dto
│   ├── ResponseDto.java
│   └── SmaLeadSecondDto.java
│
├── entity
│   └── SmaLeadSecond.java
│
├── mapper
│   └── SmaLeadMapper.java
│
├── repository
│   └── SmaLeadSecondRepository.java
│
└── services
    └── SmaLeadSecondService.java

```
### Required Excel Column Mapping

| Index | Column Name      |
| ----- | ---------------- |
| 0     | companyName      |
| 1     | contactPerson    |
| 2     | mobile           |
| 3     | mobileSecond     |
| 4     | email            |
| 5     | address          |
| 6     | city             |
| 7     | state            |
| 8     | postalCode       |
| 9     | country          |
| 10    | panNumber        |
| 11    | gstNumber        |
| 12    | businessCategory |
| 13    | personCategory   |

### Code Explained

#### Controller

```
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SmaLeadController {

    private SmaLeadSecondService smaLeadSecondService;

    @PostMapping(value = "/createSmaLead", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> createSmaLead(@RequestParam("file") MultipartFile file) {
        boolean isCreated = smaLeadSecondService.createSmaLeadSecond(file);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto("201", "Created Successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "Failed to Import"));
        }
    }
}


```

#### Response DTO

```
@Data
@AllArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
}

```

#### Lead DTO

```
@Data
@AllArgsConstructor
public class SmaLeadSecondDto {
    private Long id;
    private String companyName;
    private String contactPerson;
    private String mobile;
    private String mobileSecond;
    private String email;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String panNumber;
    private String gstNumber;
    private String businessCategory;
    private String personCategory;
}

```

#### MapStruct Mapper

```
@Mapper(componentModel = "spring")
public interface SmaLeadMapper {

    SmaLeadMapper SMA_LEAD_MAPPER = Mappers.getMapper(SmaLeadMapper.class);

    SmaLeadSecondDto mappedToSmaLeadSecondDto(SmaLeadSecond entity);

    SmaLeadSecond mappedToSmaLeadSecond(SmaLeadSecondDto dto);
}

```

#### Service (Excel Parsing + Bulk Save)

```
@Service
@AllArgsConstructor
public class SmaLeadSecondService {

    private SmaLeadSecondRepository smaLeadSecondRepository;
    private SmaLeadMapper smaLeadMapper;

    @Transactional
    public boolean createSmaLeadSecond(MultipartFile file) {
        boolean isCreated = false;

        if (file != null) {
            List<SmaLeadSecond> entities = new ArrayList<>();

            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {

                Sheet sheet = workbook.getSheetAt(0);

                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;

                    String companyName = getCellString(row.getCell(0));
                    String contactPerson = getCellString(row.getCell(1));
                    String mobile = getCellString(row.getCell(2));
                    String mobileSecond = getCellString(row.getCell(3));
                    String email = getCellString(row.getCell(4));
                    String address = getCellString(row.getCell(5));
                    String city = getCellString(row.getCell(6));
                    String state = getCellString(row.getCell(7));
                    String postalCode = getCellString(row.getCell(8));
                    String country = getCellString(row.getCell(9));
                    String panNumber = getCellString(row.getCell(10));
                    String gstNumber = getCellString(row.getCell(11));
                    String businessCategory = getCellString(row.getCell(12));
                    String personCategory = getCellString(row.getCell(13));

                    if (companyName.isEmpty() && contactPerson.isEmpty() && mobile.isEmpty()) {
                        continue;
                    }

                    SmaLeadSecondDto dto = new SmaLeadSecondDto(
                            null, companyName, contactPerson, mobile, mobileSecond, email,
                            address, city, state, postalCode, country, panNumber, gstNumber,
                            businessCategory, personCategory
                    );

                    entities.add(smaLeadMapper.mappedToSmaLeadSecond(dto));
                }

                smaLeadSecondRepository.saveAll(entities);
                isCreated = true;

            } catch (Exception e) {
                throw new RuntimeException("Failed to import Excel file: " + e.getMessage(), e);
            }
        }

        return isCreated;
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                double d = cell.getNumericCellValue();
                return (d == Math.floor(d)) ? String.valueOf((long) d) : String.valueOf(d);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (IllegalStateException ex) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
            default:
                return "";
        }
    }
}

```


#### Technologies Used

<ul>
  <li>Spring Boot</li>
  <li>Spring Web</li>
  <li>Spring Data JPA</li>
  <li>MapStruct</li>
  <li>Apache POI</li>
  <li>Lombok</li>
  <li>MySQL / PostgreSQL (any RDBMS)</li>
</ul>



#### Success Response

```
{
  "status": "201",
  "message": "Created Successfully"
}

```

#### Error Response

```
{
  "status": "404",
  "message": "Failed to Import"
}

```
