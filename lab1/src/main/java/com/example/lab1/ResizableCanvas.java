package com.example.lab1;

import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

class ResizableCanvas extends Canvas {
    private GraphicsContext gc = getGraphicsContext2D();
    private double oldWidth = getWidth();
    private double oldHeight = getHeight();
    private MainModel model = new MainModel();

    public ResizableCanvas() {
        model.setCurrent_set(SetNumber.NONE);
        setOnMouseClicked(mouseEvent -> onMouseClicked(mouseEvent));
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();
        if (width == 0 || height == 0) { return; }
        double deltaWidth = width / (oldWidth == 0 ? width : oldWidth);
        double deltaHeight = height / (oldHeight == 0 ? height : oldHeight);
        double deltaDiagonal = Math.sqrt(width * width + height * height) / Math.sqrt(oldWidth * oldWidth + oldHeight * oldHeight);
        gc.clearRect(0, 0, width, height);

        if (!model.getSet1().isEmpty()) { model.setCurrent_set(SetNumber.FIRST); }
        model.getSet1().forEach(point -> {
            scalePoint(point, deltaWidth, deltaHeight);
            drawPoint(point);
        });
        if (!model.getSet2().isEmpty()) { model.setCurrent_set(SetNumber.SECOND);}
        model.getSet2().forEach(point -> {
            scalePoint(point, deltaWidth, deltaHeight);
            drawPoint(point);
        });
//        model.getRes().forEach(point -> {
//            scalePoint(point, deltaWidth, deltaHeight);
//        });
        Oval oval = model.getOval();
        scaleOval(oval, deltaWidth, deltaHeight);
        drawOval(model.getOval());

        oldWidth = width;
        oldHeight = height;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    void inputFirstSetBtnDidTap(ActionEvent event) {
        model.setCurrent_set(SetNumber.FIRST);
    }

    void inputSecondSetBtnDidTap(ActionEvent event) {
        model.setCurrent_set(SetNumber.SECOND);
    }

    void cancelBtnDidTap() {

    }

    void calculateBtnDidTap() {
        Circle circle = model.calculateBtnDidTap();
        drawCircle(circle);
    }

    private void onMouseClicked(MouseEvent event) {
        Point point = new Point((int)event.getX(), (int)event.getY());
        drawPoint(point);
        model.addToSet(point);
    }

    private void drawCircle(Circle circle) {
        gc.setFill(Color.rgb(0, 128, 0, 0.3));
        double radius = circle.getRadius();
        double diameter = circle.getRadius() * 2;
        gc.fillOval(circle.getCenter().getX() - radius, circle.getCenter().getY() - radius, diameter, diameter);
    }

    private void drawPoint(Point point) {
        switch (model.getCurrent_set()) {
            case NONE -> gc.setFill(Color.WHITE);
            case FIRST -> gc.setFill(Color.RED);
            case SECOND -> gc.setFill(Color.BLUE);
        }
        gc.fillOval(
                point.getX() - Constants.pointRadius,
                point.getY() - Constants.pointRadius,
                Constants.pointDiameter,
                Constants.pointDiameter
        );
    }

    private void drawOval(Oval oval) {
        gc.setFill(Color.rgb(0, 128, 0, 0.3));
        double width = oval.getWidth();
        double height = oval.getHeight();
        gc.fillOval(oval.getCenter().getX() - width / 2, oval.getCenter().getY() - height / 2, width, height);
    }

    private void scalePoint(Point point, double deltaX, double deltaY) {
        point.setX(point.getX() * deltaX);
        point.setY(point.getY() * deltaY);
    }

    private void scaleOval(Oval oval, double deltaX, double deltaY) {
        Point center = oval.getCenter();
        oval.setCenter(new Point(center.getX() * deltaX, center.getY() * deltaY));
        oval.setWidth(oval.getWidth() * deltaX);
        oval.setHeight(oval.getHeight() * deltaY);
    }
}

class Constants {
    static double pointDiameter = 6.0;
    static double pointRadius = Constants.pointDiameter / 2;
}
