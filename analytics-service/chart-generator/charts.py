from flask import Flask, request, send_file, abort
import matplotlib.pyplot as plt
import io
import json
from collections import Counter
from datetime import datetime

app = Flask(__name__)

@app.route('/generate', methods=['POST'])
def generate_chart():
    chart_type = request.json.get("chartType")
    data = request.json.get("data")

    if not chart_type or not data:
        return abort(400, "Missing chartType or data in request")

    try:
        fig, ax = plt.subplots(figsize=(8, 5))

        if chart_type == "TheMostPopularRecipies":
            counter = Counter(data)
            names, values = zip(*counter.most_common())
            ax.barh(names, values)
            ax.set_title("Najpopularniejsze drinki")
            ax.set_xlabel("Liczba przygotowań")

        elif chart_type == "TheMostPopularIngredients":
            counter = Counter(data)
            names, values = zip(*counter.most_common())
            ax.barh(names, values)
            ax.set_title("Najczęściej używane składniki")
            ax.set_xlabel("Liczba użyć")

        elif chart_type == "ConsuptionInTime":
            date_counts = Counter(data.values())
            sorted_dates = sorted(date_counts.keys(), key=lambda d: datetime.strptime(d, "%Y-%m-%d"))
            counts = [date_counts[date] for date in sorted_dates]
            ax.bar(sorted_dates, counts)
            ax.set_title("Liczba przygotowanych drinków w czasie")
            ax.set_xlabel("Data")
            ax.set_ylabel("Liczba drinków")

        else:
            return abort(400, f"Unsupported chart type: {chart_type}")

        plt.tight_layout()
        img_io = io.BytesIO()
        plt.savefig(img_io, format='png')
        img_io.seek(0)
        plt.close(fig)

        return send_file(img_io, mimetype='image/png')

    except Exception as e:
        return abort(500, f"Error generating chart: {str(e)}")

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)