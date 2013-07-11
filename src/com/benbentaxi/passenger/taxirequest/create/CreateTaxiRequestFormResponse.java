package com.benbentaxi.passenger.taxirequest.create;

import com.benbentaxi.api.FormResponse;
import com.benbentaxi.api.ViewForm;

public class CreateTaxiRequestFormResponse extends FormResponse{

	public CreateTaxiRequestFormResponse(ViewForm viewForm, String rStr) {
		super(viewForm, rStr);
	}

	@Override
	public void parser() {
	}
}
