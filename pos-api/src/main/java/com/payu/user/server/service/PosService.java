package com.payu.user.server.service;

import com.payu.user.server.model.Pos;



public interface PosService {

    void createPos(Pos pos);

    public Pos getPosById(Long id);

	public abstract int deletePoses();

	public void activatePos(long userId);

}
