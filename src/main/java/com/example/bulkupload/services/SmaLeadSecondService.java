package com.example.bulkupload.services;

import com.example.bulkupload.dto.SmaLeadSecondDto;
import com.example.bulkupload.entity.SmaLeadSecond;
import com.example.bulkupload.mapper.SmaLeadMapper;
import com.example.bulkupload.repository.SmaLeadSecondRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SmaLeadSecondService {

    private SmaLeadSecondRepository smaLeadSecondRepository;
    private SmaLeadMapper smaLeadMapper;

    @Transactional
    public boolean createSmaLeadSecond(MultipartFile file){
        boolean isCreated = false;
        if(file !=  null){

            List<SmaLeadSecond> entitiesToSave = new ArrayList<>();
            int processed = 0;

            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {

                Sheet sheet = workbook.getSheetAt(0);

                // Assuming first row is header, start from row 1

                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;

                    // Read values by column index (adjust indices to your sheet)
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

                    // skip completely empty rows (optional)
                    if (companyName.isEmpty() && contactPerson.isEmpty() && mobile.isEmpty()) {
                        continue;
                    }

                    // Build DTO (or build entity directly)
                    SmaLeadSecondDto dto = new SmaLeadSecondDto(
                            null, companyName, contactPerson, mobile, mobileSecond, email,
                            address, city, state, postalCode, country, panNumber, gstNumber,
                            businessCategory, personCategory
                    );

                    SmaLeadSecond entity = smaLeadMapper.mappedToSmaLeadSecond(dto);
                    entitiesToSave.add(entity);
                    processed++;
                }

                if (!entitiesToSave.isEmpty()) {
                    smaLeadSecondRepository.saveAll(entitiesToSave);
                }
                isCreated = true;

            } catch (Exception e) {
                throw new RuntimeException("Failed to import Excel file: " + e.getMessage(), e);
            }
        }
        return isCreated;
    }

    // Helper to read a cell as String safely
    private String getCellString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Remove .0 for integer-like numbers
                double d = cell.getNumericCellValue();
                if (d == Math.floor(d)) {
                    return String.valueOf((long) d);
                } else {
                    return String.valueOf(d);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (IllegalStateException ex) {
                    double dv = cell.getNumericCellValue();
                    return String.valueOf(dv);
                }
            case BLANK:
            default:
                return "";
        }
    }

}
