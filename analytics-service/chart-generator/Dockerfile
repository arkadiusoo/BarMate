FROM python:3.11-slim

WORKDIR /app

COPY charts.py .

RUN pip install flask matplotlib

EXPOSE 5000

CMD ["python", "charts.py"]