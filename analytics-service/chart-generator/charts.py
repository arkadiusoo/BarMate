from flask import Flask, request, send_file, abort
import matplotlib.pyplot as plt
import io
import json

app = Flask(__name__)

@app.route('/generate', methods=['POST'])
def generate_chart():
    chart_type = request.args.get("type")
    if not chart_type:
        return abort(400, "Missing 'type' query parameter")

    try:
        data = request.get_json()
        fig, ax = plt.subplots(figsize=(8, 5))

        if chart_type == "activity":
            dates = data["dates"]
            counts = data["counts"]
            ax.bar(dates, counts)
            ax.set_title("Drink Preparation Activity")
            ax.set_xlabel("Date")
            ax.set_ylabel("Drinks Made")

        elif chart_type == "top5":
            names = data["recipeNames"]
            values = data["preparationCounts"]
            ax.barh(names, values)
            ax.set_title("Top 5 Most Prepared Drinks")
            ax.set_xlabel("Times Prepared")

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