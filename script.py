import pdfplumber
import pandas as pd
# Use pdfplumber to extract text and tables from the PDF
pdf_path = "./airtable-sample.pdf"
pdf = pdfplumber.open(pdf_path)

# Extract text and tables from each page
extracted_data = []
for page in pdf.pages:
    tables = page.extract_table()
    if tables:
        extracted_data.extend(tables)

pdf.close()

# Convert extracted data to a pandas DataFrame
df = pd.DataFrame(extracted_data[1:], columns=extracted_data[0])

# Save DataFrame to CSV
csv_path = "./airtable-data.csv"
df.to_csv(csv_path, index=False)

csv_path
