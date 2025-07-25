#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to print test results
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Test Hello World endpoint
echo "Testing Hello World endpoint..."
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/v1/helloworld")
status_code=$(echo "$response" | tail -n1)
content=$(echo "$response" | sed '$d')
if [ "$status_code" -eq 200 ] && [ "$content" == "Hello, World!" ]; then
    print_result 0 "Hello World endpoint"
else
    print_result 1 "Hello World endpoint"
fi

# Test Book endpoints
echo -e "\nTesting Book endpoints..."

# Create a book
echo "Creating a book..."
book_data='{"title":"Test Book","author":"Test Author","publishedDate":"2024-01-01"}'
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/books" \
    -H "Content-Type: application/json" \
    -d "$book_data")
status_code=$(echo "$response" | tail -n1)
if [ "$status_code" -eq 200 ]; then
    print_result 0 "Create book"
    # Extract the book ID from the response (if needed)
    book_id=$(echo "$response" | sed '$d' | jq -r '.id' 2>/dev/null)
else
    print_result 1 "Create book"
fi

# Get all books
echo "Getting all books..."
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/books")
status_code=$(echo "$response" | tail -n1)
if [ "$status_code" -eq 200 ]; then
    print_result 0 "Get all books"
else
    print_result 1 "Get all books"
fi

# Get a specific book
echo "Getting a specific book..."
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/books/1")
status_code=$(echo "$response" | tail -n1)
if [ "$status_code" -eq 200 ]; then
    print_result 0 "Get specific book"
else
    print_result 1 "Get specific book"
fi

# Update a book
echo "Updating a book..."
update_data='{"title":"Updated Book","author":"Updated Author","publishedDate":"2024-02-01"}'
response=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/api/books/1" \
    -H "Content-Type: application/json" \
    -d "$update_data")
status_code=$(echo "$response" | tail -n1)
if [ "$status_code" -eq 200 ]; then
    print_result 0 "Update book"
else
    print_result 1 "Update book"
fi

# Delete a book
echo "Deleting a book..."
response=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/api/books/1")
status_code=$(echo "$response" | tail -n1)
if [ "$status_code" -eq 204 ]; then
    print_result 0 "Delete book"
else
    print_result 1 "Delete book"
fi

echo -e "\nAll tests completed!" 